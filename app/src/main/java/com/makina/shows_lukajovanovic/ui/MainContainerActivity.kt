package com.makina.shows_lukajovanovic.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeFragment
import com.makina.shows_lukajovanovic.ui.episodes.add.SeasonEpisodePickerDialog
import com.makina.shows_lukajovanovic.ui.episodes.add.TakePhotoDialog
import com.makina.shows_lukajovanovic.ui.shows.LogoutConfirmDialogFragment
import com.makina.shows_lukajovanovic.ui.shows.ShowsFragment

class MainContainerActivity : AppCompatActivity(),
	SeasonEpisodePickerDialog.NoticeDialogListener,
	TakePhotoDialog.TakePhotoDialogListener,
	LogoutConfirmDialogFragment.LogoutDialogListener,
	ShowsFragment.ShowsFragmentContainer,
	EpisodesFragment.EpisodesFragmentContainer {

	companion object {
		fun newInstance(context: Context): Intent {
			return Intent(context, MainContainerActivity::class.java)
		}
	}

	var slaveContainerId: Int = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main_container)
		slaveContainerId = R.id.containerSlave

		//ovaj upit mi je potreban jer se inace pozove onViewCreated u ShowsFragmentu dva puta za redom s tim da u drugom pozivu je savedInstaceState uvijek null
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction().apply {
				replace(R.id.containerMaster, ShowsFragment(), ShowsFragment.SHOWS_FRAGMENT_TAG)
				commit()
			}
		}
	}


	override fun onDialogSaveButton(dialog: SeasonEpisodePickerDialog) {
		(supportFragmentManager.findFragmentByTag(AddEpisodeFragment.ADD_EPISODE_TAG) as? AddEpisodeFragment)
			?.onDialogSaveButton(dialog)
	}

	override fun onDialogSelect(which: String) {
		(supportFragmentManager.findFragmentByTag(AddEpisodeFragment.ADD_EPISODE_TAG) as? AddEpisodeFragment)
			?.onDialogSelect(which)
	}

	override fun responseLogout() {
		(supportFragmentManager.findFragmentByTag(ShowsFragment.SHOWS_FRAGMENT_TAG) as? ShowsFragment)
			?.responseLogout()
	}

	override fun startEpisodesFragment(showId: String, title: String, likesNumber: Int) {
		while(supportFragmentManager?.popBackStackImmediate() == true);
		supportFragmentManager?.beginTransaction()?.apply {
			replace(
				slaveContainerId,
				EpisodesFragment.newInstance(showId, title, likesNumber),
				EpisodesFragment.EPISODES_FRAGMENT_TAG
			)
			addToBackStack("Episodes $showId")
			commit()
		}
	}
	override fun startAddEpisodeFragment(showId: String) {
		supportFragmentManager?.beginTransaction()?.apply {
			replace(
				slaveContainerId,
				AddEpisodeFragment.newInstance(showId),
				AddEpisodeFragment.ADD_EPISODE_TAG
			)
			addToBackStack("AddEpisodesFragment")
			commit()
		}
	}

	override fun startEpisodeDetailsFragment() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}
