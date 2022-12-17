package com.victorvalencia.readdit.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.readdit.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.HttpURLConnection

abstract class BaseViewModel: ViewModel() {

    private val uiEventFlow: SharedFlow<BaseUiEvent> = MutableSharedFlow()

    sealed class BaseUiEvent {
        data class ShowLoadingEvent(val showLoading: Boolean): BaseUiEvent()
        data class ShowErrorDialogEvent(val dialogContent: DialogContent): BaseUiEvent()
    }

    /** Helper function to avoid needing downcast declarations for public MutableStateFlow. [value] only set when it is non-nullable */
    protected fun <T : Any> StateFlow<T>?.set(value: T?) {
        if (this is MutableStateFlow<T> && value != null) {
            this.value = value
        } else {
            Timber.w("[set] unable to set value for $this")
        }
    }

    /** Helper function to avoid needing downcast declarations for public MutableSharedFlow. [value] only emitted when it is non-nullable */
    suspend fun <T : Any> SharedFlow<T>?.emitValue(value: T?) {
        if (this is MutableSharedFlow<T> && value != null) {
            emit(value)
        } else {
            Timber.w("[emitValue] unable to emit value for $this")
        }
    }

    fun observeUiEvents(fragment: BaseFragment<*, *>) {
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiEventFlow.collect { event ->
                    when (event) {
                        is BaseUiEvent.ShowLoadingEvent -> fragment.showLoading(event.showLoading)
                        is BaseUiEvent.ShowErrorDialogEvent -> fragment.showErrorDialog(event.dialogContent)
                    }
                }
            }
        }
    }

    private suspend fun showErrorDialogEvent(dialogContent: DialogContent) {
        uiEventFlow.emitValue(BaseUiEvent.ShowErrorDialogEvent(dialogContent))
    }

    private suspend fun showLoadingEvent(showLoading: Boolean) {
        uiEventFlow.emitValue(BaseUiEvent.ShowLoadingEvent(showLoading))
    }

    protected suspend fun <T> wrapWithLoadingAndErrorEvents(
        showLoading: Boolean = true,
        showErrorDialog: Boolean = true,
        block: suspend () -> T
    ): T {
        if (showLoading) showLoadingEvent(true)
        val result = block()
        when {
            result is ApiResult.Failure.NetworkTimeoutFailure && result.noConnection -> {
                showErrorDialogEvent(
                    DialogContent(
                        title = R.string.all_error_dialog_no_connection_title,
                        message = R.string.all_error_dialog_no_connection_message
                    )
                )
            }
            result is ApiResult.Failure.Server && result.error?.httpErrorCode == HttpURLConnection.HTTP_NOT_FOUND -> {
                showErrorDialogEvent(
                    DialogContent(
                        title = R.string.all_error_dialog_no_found_title,
                        message = R.string.all_error_dialog_no_found_message
                    )
                )
            }
            result is ApiResult.Failure.Server && result.error?.httpErrorCode == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                showErrorDialogEvent(
                    DialogContent(
                        title = R.string.all_error_dialog_unauthorized_title,
                        message = R.string.all_error_dialog_unauthorized_message
                    )
                )
            }
            result is ApiResult.Failure && showErrorDialog -> {
                showErrorDialogEvent(DialogContent())
            }
        }
        if (showLoading) showLoadingEvent(false)
        return result
    }
}