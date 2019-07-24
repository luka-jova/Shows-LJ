package com.makina.shows_lukajovanovic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import com.makina.shows_lukajovanovic.ui.shows.ShowsFragment
import kotlinx.android.synthetic.main.activity_main_container.*

class MainContainerActivity : AppCompatActivity() {

	var mSlaveContainerId: Int = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main_container)
		Log.d("tigar", "Master je na ${R.id.containerMaster}")
		if(containerSlave != null) {
			Log.d("tigar", "postavljam Slave na ${R.id.containerSlave}")
			mSlaveContainerId = R.id.containerSlave
		}
		else {
			Log.d("tigar", "postavljam Slave na MASTER: ${R.id.containerMaster}")
			mSlaveContainerId = R.id.containerMaster
		}

		supportFragmentManager.beginTransaction().apply {
			//TODO CIJI JE TOCNO OVO id? OD INSTANCE? OD "KLASE"?
			add(R.id.containerMaster, ShowsFragment())
			commit()
		}
		updateVisibility()
	}

	fun updateVisibility() {
		Log.d("tigar", "updateVisibility")
		if(MODE_PORTRAIT != null) {
			///PORTRAIT MODE
			Log.d("tigar", "portrait")
			if(supportFragmentManager.findFragmentByTag(EpisodesFragment.EPISODES_FRAGMENT_TAG) != null) {
				Log.d("tigar", "imam EPISODES_FRAGMENT_TAG")
				containerMaster.visibility = View.INVISIBLE
				containerSlave.visibility = View.VISIBLE
			}
			else {
				Log.d("tigar", "nemam EPISODES_FRAGMENT_TAG")
				containerMaster.visibility = View.VISIBLE
				containerSlave.visibility = View.INVISIBLE
			}
		}
		else {
			///LANDSCAPE TABLET MODE
			Log.d("tigar", "landscape tablet")
			containerMaster.visibility = View.VISIBLE
			containerSlave.visibility = View.VISIBLE
		}
	}

	override fun onBackPressed() {
		super.onBackPressed()
		updateVisibility()
	}
}
