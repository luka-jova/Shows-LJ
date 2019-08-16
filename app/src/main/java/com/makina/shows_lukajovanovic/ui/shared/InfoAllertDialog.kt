package com.makina.shows_lukajovanovic.ui.shared

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class InfoAllertDialog : DialogFragment() {
	companion object {
		private const val MESSAGE_CODE = "MESSAGE_CODE"
		private const val TITLE_CODE = "TITLE_CODE"
		fun newInstance(title: String, message: String): InfoAllertDialog {
			return InfoAllertDialog().apply {
				arguments = Bundle().apply {
					putString(MESSAGE_CODE, message)
					putString(TITLE_CODE, title)
				}
			}
		}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			AlertDialog.Builder(it)
				.setTitle(arguments?.getString(TITLE_CODE) ?: "")
				.setMessage(arguments?.getString(MESSAGE_CODE) ?: "Something happened")
				.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ -> })
				.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}
}