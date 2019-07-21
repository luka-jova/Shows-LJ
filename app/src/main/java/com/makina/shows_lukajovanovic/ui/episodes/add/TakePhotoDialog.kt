package com.makina.shows_lukajovanovic.ui.episodes.add

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TakePhotoDialog: DialogFragment() {
	companion object {
		const val CAMERA_STRING = "Camera"
		const val GALLERY_STRING = "Gallery"
		val arrayCodes: Array<String> = arrayOf(
			CAMERA_STRING,
			GALLERY_STRING
		)
	}
	internal lateinit var listener: TakePhotoDialogListener

	interface TakePhotoDialogListener {
		fun onDialogSelect(which: String)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			// Use the Builder class for convenient dialog construction
			val builder = AlertDialog.Builder(it)
			builder.setItems(arrayCodes, DialogInterface.OnClickListener { dialog, which ->
					listener.onDialogSelect(arrayCodes[ which ])
				})
			// Create the AlertDialog object and return it
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = context as TakePhotoDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException(("$context must implement NoticeDialogListener"))
		}

	}
}