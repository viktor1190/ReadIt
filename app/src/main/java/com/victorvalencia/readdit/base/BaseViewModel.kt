package com.victorvalencia.readdit.base

import androidx.lifecycle.ViewModel
import com.victorvalencia.data.model.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.net.HttpURLConnection

abstract class BaseViewModel: ViewModel() {

    /** Helper function to avoid needing downcast declarations for public MutableStateFlow. [value] only set when it is non-nullable */
    protected fun <T : Any> StateFlow<T>?.set(value: T?) {
        if (this is MutableStateFlow<T> && value != null) {
            this.value = value
        } else {
            Timber.w("[set] unable to set value for $this")
        }
    }

    suspend fun showNoConnectionEvent() {
        TODO()
    }

    suspend fun showNotFoundFailureEvent() {
        TODO()
    }

    suspend fun showUnauthorizedFailureEvent() {
        TODO()
    }

    suspend fun showErrorDialogEvent() {
        TODO()
    }

    suspend fun showLoadingEvent(showLoading: Boolean) {
        TODO()
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
                showNoConnectionEvent()
            }
            result is ApiResult.Failure.Server && result.error?.httpErrorCode == HttpURLConnection.HTTP_NOT_FOUND -> {
                showNotFoundFailureEvent()
            }
            result is ApiResult.Failure.Server && result.error?.httpErrorCode == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                showUnauthorizedFailureEvent()
            }
            result is ApiResult.Failure && showErrorDialog -> {
                showErrorDialogEvent()
            }
        }
        if (showLoading) showLoadingEvent(false)
        return result
    }
}