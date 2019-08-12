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
import com.makina.shows_lukajovanovic.ui.shows.LogoutConfirmDialogFragment
import com.makina.shows_lukajovanovic.ui.shows.ShowsFragment
import kotlinx.android.synthetic.main.activity_main_container.*

class MainContainerActivity : AppCompatActivity(),
	SeasonEpisodePickerDialog.NoticeDialogListener,
	TakePhotoDialog.TakePhotoDialogListener, LogoutConfirmDialogFragment.LogoutDialogListener {

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

		supportFragmentManager.beginTransaction().apply {
			replace(R.id.containerMaster, ShowsFragment(), ShowsFragment.SHOWS_FRAGMENT_TAG)
			commit()
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

}
