package com.makina.shows_lukajovanovic

import android.app.Application

class ShowsApp : Application() {
	companion object {
		lateinit var instance: ShowsApp
	}


	override fun onCreate() {
		super.onCreate()
		instance = this
	}
}