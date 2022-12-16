package com.victorvalencia.readdit.base

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.victorvalencia.readdit.R

class SimpleDialogFragment(private val dialogContent: DialogContent): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(dialogContent.title ?: R.string.all_error_dialog_generic_title)
            .setMessage(dialogContent.message ?: R.string.all_error_dialog_generic_message)
            .setPositiveButton(
                dialogContent.buttonContent?.positiveTitle ?: android.R.string.ok
            ) { _, _ -> dialogContent.buttonContent?.positiveClick?.invoke() }
            .setNegativeButton(
                dialogContent.buttonContent?.negativeTitle ?: android.R.string.cancel,
            ) { _, _ ->
                dialogContent.buttonContent?.negativeClick?.invoke()
            }
            .create()
        return dialog
    }
}

data class DialogContent(
    @StringRes val title: Int? = null,
    @StringRes val message: Int? = null,
    val buttonContent: ButtonContent? = null
) {
    data class ButtonContent(
        @StringRes val positiveTitle: Int? = null,
        @StringRes val negativeTitle: Int? = null,
        val positiveClick: (() -> Unit)? = null,
        val negativeClick: (() -> Unit)? = null
    )
}