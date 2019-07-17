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
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*


class SeasonEpisodePickerDialog(var curSeason: Int = 1, var curEpisode: Int = 1) : DialogFragment() {
	companion object {
		const val CUR_SEASON = "CUR_SEASON"
		const val CUR_EPISODE = "CUR_EPISODE"
	}
	lateinit var curView : View
	init{
		//TODO zasto se kod rotacije poziva ovaj init? -> da se ne poziva ne bih morao koristiti Bundle
		Log.d("moj tag", "inicijalizacija")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		Log.d("moj tag", "sejvam $curSeason $curEpisode")
		outState.putInt(CUR_SEASON, curSeason)
		outState.putInt(CUR_EPISODE, curEpisode)
		super.onSaveInstanceState(outState)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		Log.d("moj tag", "onCreateDialog()")
		if(curSeason == -1) curSeason = 1
		if(curEpisode == -1) curEpisode = 1
		curSeason = savedInstanceState?.getInt(CUR_SEASON)  ?: curSeason
		curEpisode = savedInstanceState?.getInt(CUR_EPISODE) ?: curEpisode
		Log.d("moj tag", "loadam $curSeason $curEpisode")

		return activity?.let {
			//TODO sto radi activity.let
			val builder = AlertDialog.Builder(it)
			val inflater = requireActivity().layoutInflater

			//TODO mogu li povecati font texta u neutral buttonu?
			curView = inflater.inflate(R.layout.layout_fragment_season_episode_picker, null)
			builder.setView(curView)
			Log.d("moj tag", "prvotno stanje je ${view == null}")
			builder.setPositiveButton(R.string.text_save,
					DialogInterface.OnClickListener { dialog, id ->
						listener.onDialogSaveButton(this@SeasonEpisodePickerDialog)
					})

			curView.numberPickerSeason.maxValue = resources.getInteger(R.integer.int_season_max)
			curView.numberPickerSeason.minValue = resources.getInteger(R.integer.int_season_min)
			curView.numberPickerEpisode.minValue = resources.getInteger(R.integer.int_episode_min)
			curView.numberPickerEpisode.maxValue = resources.getInteger(R.integer.int_episode_max)
			curView.numberPickerEpisode.value = curEpisode
			curView.numberPickerSeason.value = curSeason

			curView.numberPickerSeason.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
				override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
					this@SeasonEpisodePickerDialog.curSeason = newVal
				}
			})
			curView.numberPickerEpisode.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
				override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
					this@SeasonEpisodePickerDialog.curEpisode = newVal
				}
			})

			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")

	}

	internal lateinit var listener: NoticeDialogListener

	interface NoticeDialogListener {
		//TODO prije je radilo, a sad ne radi da ovdje kao argument primam dialog: DialogFragment, i onda overrideam dialog: SeasonEpisodePickerDialog (sto je takoder DialogFragment)
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
