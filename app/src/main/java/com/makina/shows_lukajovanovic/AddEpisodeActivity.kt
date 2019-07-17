package com.makina.shows_lukajovanovic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.TextureView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.*
import kotlinx.android.synthetic.main.layout_fragment_season_episode_picker.view.*

class AddEpisodeActivity : AppCompatActivity(), SeasonEpisodePickerDialog.NoticeDialogListener {

	companion object {
		const val EPISODE_CODE = "EPISODE_CODE"
		fun newInstance(context: Context) : Intent {
			val intent = Intent(context, AddEpisodeActivity::class.java)
			return intent
		}
	}
	private var curEpisode:Episode = Episode()

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putSerializable(EPISODE_CODE, curEpisode)
		super.onSaveInstanceState(outState)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		///OVO JE TEORETSKI VISKA, ALI OSTAVLJAM ZATO STO AKO ZELIM U TEKST POSTAVITI NESTO SVOJE (NE SEJVAN TEKST) ONDA AKO POZIVAM SAMO U onCreate-u TO POSTAVLJANJE TEKSTA, super(...) u ovoja metodi CE UBACITI ZAPRAVO SEJVANI TEKST TE PREBRISATI ONO STO SAM STAVIO U onCreate-u
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_episode)
		setSupportActionBar(toolbarAddEpisode)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = "Add episode"

		curEpisode = (savedInstanceState?.getSerializable(EPISODE_CODE) as? Episode) ?: Episode()

		//**LOAD STATE*//
		editTextEpisodeName.setText(curEpisode.name)
		editTextEpisodeDescription.setText(curEpisode.episodeDescription)
		loadTextViewSE()


		buttonSave.setOnClickListener {
			val resultIntent = Intent()
			resultIntent.putExtra(EPISODE_CODE, curEpisode)
			setResult(RESULT_OK, resultIntent)
			finish()
		}
		linearLayoutSeasonEpisodePicker.setOnClickListener {
			Log.d("moj tag", "otvaram Fragment ${curEpisode.seasonNum} ${curEpisode.episodeNum}")
			SeasonEpisodePickerDialog(curEpisode.seasonNum, curEpisode.episodeNum).show(supportFragmentManager, "timePicker")
		}

		editTextEpisodeName.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.name = this@AddEpisodeActivity.editTextEpisodeName.text.toString()
			}
		})
		editTextEpisodeDescription.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				curEpisode.episodeDescription = this@AddEpisodeActivity.editTextEpisodeDescription.text.toString()
			}
		})
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		//TODO dialog.view uopce nije dobro postavljen: if(dialog.view?.editTextNapisiNesto == null) {Log.d("moj tag", "editTextNapisiNesto je null")}
		curEpisode.episodeNum = dialog.curView.numberPickerEpisode.value
		curEpisode.seasonNum = dialog.curView.numberPickerSeason.value
		loadTextViewSE()
	}

	fun loadTextViewSE() {
		var cE:String = "--"
		if(curEpisode.episodeNum != -1) cE = curEpisode.episodeNum.toString()
		var cS:String = "--"
		if(curEpisode.seasonNum != -1) cS = curEpisode.seasonNum.toString()
		textViewSeasonEpisode.text = "S ${cS.padStart(2, '0')}, E ${cE.padStart(2, '0')}"
	}

}
