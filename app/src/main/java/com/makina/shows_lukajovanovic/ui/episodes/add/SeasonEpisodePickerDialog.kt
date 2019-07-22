package com.makina.shows_lukajovanovic.ui.episodes.add

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.makina.shows_lukajovanovic.R
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*


class SeasonEpisodePickerDialog(var curSeason: Int = 1, var curEpisode: Int = 1) : DialogFragment() {
	companion object {
		const val CUR_SEASON = "CUR_SEASON"
		const val CUR_EPISODE = "CUR_EPISODE"
	}
	lateinit var curView : View

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putInt(CUR_SEASON, curSeason)
		outState.putInt(CUR_EPISODE, curEpisode)
		super.onSaveInstanceState(outState)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		if(curSeason == -1) curSeason = 1
		if(curEpisode == -1) curEpisode = 1
		curSeason = savedInstanceState?.getInt(CUR_SEASON)  ?: curSeason
		curEpisode = savedInstanceState?.getInt(CUR_EPISODE) ?: curEpisode

		return activity?.let {
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater

			//TODO mogu li povecati font texta u neutral buttonu?
			curView = inflater.inflate(R.layout.layout_fragment_season_episode_picker, null)
			builder.setView(curView)
			builder.setPositiveButton(
				R.string.text_save,
					DialogInterface.OnClickListener { dialog, id ->
						listener.onDialogSaveButton(this@SeasonEpisodePickerDialog)
					})

			with(curView) {
				numberPickerSeason.maxValue = resources.getInteger(R.integer.int_season_max)
				numberPickerSeason.minValue = resources.getInteger(R.integer.int_season_min)
				numberPickerEpisode.minValue = resources.getInteger(R.integer.int_episode_min)
				numberPickerEpisode.maxValue = resources.getInteger(R.integer.int_episode_max)
				numberPickerEpisode.value = curEpisode
				numberPickerSeason.value = curSeason
				numberPickerSeason.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
					override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
						this@SeasonEpisodePickerDialog.curSeason = newVal
					}
				})
				numberPickerEpisode.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
					override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
						this@SeasonEpisodePickerDialog.curEpisode = newVal
					}
				})
			}

			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")

	}

	internal lateinit var listener: NoticeDialogListener

	interface NoticeDialogListener {
		fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = context as NoticeDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException(("$context must implement NoticeDialogListener"))
		}
	}
}
