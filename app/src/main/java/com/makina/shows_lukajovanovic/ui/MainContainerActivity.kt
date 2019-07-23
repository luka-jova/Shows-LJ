package com.makina.shows_lukajovanovic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.makina.shows_lukajovanovic.R
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
			Log.d("tigar", "postavljam Slave na ${R.id.containerMaster}")
			mSlaveContainerId = R.id.containerMaster
		}
		supportFragmentManager.beginTransaction().apply {
			//TODO CIJI JE TOCNO OVO id? OD INSTANCE? OD "KLASE"?
			add(R.id.containerMaster, ShowsFragment())
			commit()
		}
	}
}
