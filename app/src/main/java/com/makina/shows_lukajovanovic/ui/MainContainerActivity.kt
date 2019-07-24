package com.makina.shows_lukajovanovic.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeFragment
import com.makina.shows_lukajovanovic.ui.episodes.add.SeasonEpisodePickerDialog
import com.makina.shows_lukajovanovic.ui.episodes.add.TakePhotoDialog
import com.makina.shows_lukajovanovic.ui.shows.ShowsFragment
import kotlinx.android.synthetic.main.activity_main_container.*

class MainContainerActivity : AppCompatActivity(),
	SeasonEpisodePickerDialog.NoticeDialogListener,
	TakePhotoDialog.TakePhotoDialogListener {

	companion object {
		fun newInstance(context: Context): Intent {
			return Intent(context, MainContainerActivity::class.java)
		}
	}

	var mSlaveContainerId: Int = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main_container)
		if(containerSlave != null) {
			mSlaveContainerId = R.id.containerSlave
		}
		else {
			mSlaveContainerId = R.id.containerMaster
		}

		supportFragmentManager.beginTransaction().apply {
			//TODO CIJI JE TOCNO OVO id? OD INSTANCE? OD "KLASE"?
			replace(R.id.containerMaster, ShowsFragment())
			commit()
		}
		updateVisibility()
	}

	fun updateVisibility() {
		if(MODE_PORTRAIT != null) {
			///PORTRAIT MODE
			if(supportFragmentManager.findFragmentByTag(EpisodesFragment.EPISODES_FRAGMENT_TAG) != null) {
				containerMaster.visibility = View.INVISIBLE
				containerSlave.visibility = View.VISIBLE
			}
			else {
				containerMaster.visibility = View.VISIBLE
				containerSlave.visibility = View.INVISIBLE
			}
		}
		else {
			///LANDSCAPE TABLET MODE
			containerMaster.visibility = View.VISIBLE
			containerSlave.visibility = View.VISIBLE
		}
	}

	override fun onBackPressed() {
		super.onBackPressed()
		updateVisibility()
	}


	///TODO mogu li ikako direktno pozvati ove fje u fragmentu (ne preko Activity-a)?
	override fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		(supportFragmentManager.findFragmentByTag(AddEpisodeFragment.ADD_EPISODE_TAG) as? AddEpisodeFragment)
			?.onDialogSaveButton(dialog)
	}

	override fun onDialogSelect(which: String) {
		(supportFragmentManager.findFragmentByTag(AddEpisodeFragment.ADD_EPISODE_TAG) as? AddEpisodeFragment)
			?.onDialogSelect(which)
	}

}
