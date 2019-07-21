package com.makina.shows_lukajovanovic

import android.app.Application
import android.util.Log

class ShowsApp : Application() {
	companion object {
		lateinit var instance: ShowsApp
	}


	override fun onCreate() {
		Log.d("tigar", "onCreate")
		super.onCreate()
		instance = this
	}

	override fun onTerminate() {
		Log.d("tigar", "onTerminate")
		super.onTerminate()
	}
}