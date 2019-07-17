package com.makina.shows_lukajovanovic

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.*
import java.util.*

class SeasonEpisodePickerDialog : DialogFragment() {
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			//TODO sto radi activity.let
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater

			//TODO mogu li povecati font texta u neutral buttonu?
			builder.setView(inflater.inflate(R.layout.layout_fragment_season_episode_picker, null))
				.setPositiveButton(R.string.text_save,
					DialogInterface.OnClickListener { dialog, id ->
						/**val curFragment = activity?.supportFragmentManager?.findFragmentByTag("timePicker")
						if(curFragment == null) {Log.d("moj tag", "1. null")}
						else if(curFragment.editTextNapisiNesto == null) {Log.d("moj tag", "2. null")}
						else {Log.d("moj tag", curFragment.editTextNapisiNesto.text.toString())}**/

						///ipak je ova klasa takoder Fragment
						/**Log.d("moj tag", "bok")
						Log.d("moj tag", "debug ${this@SeasonEpisodePickerDialog.editTextNapisiNesto.text}")
						*/
						listener.onDialogSaveButton(this)
					})
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")

	}

	// Use this instance of the interface to deliver action events
	internal lateinit var listener: NoticeDialogListener

	interface NoticeDialogListener {
		fun onDialogSaveButton(dialog: DialogFragment)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = context as NoticeDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException(("$context must implement NoticeDialogListener"))
		}
	}

	override fun onResume() {
		super.onResume()
		view.findViewById(R.id.editTextNapisiNesto)
		Log.d("moj tag", "kad je stvoreno pokusavam ${editTextNapisiNesto.text}")
	}
}
