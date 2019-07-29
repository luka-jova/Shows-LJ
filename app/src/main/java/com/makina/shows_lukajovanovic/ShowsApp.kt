package com.makina.shows_lukajovanovic

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.makina.shows_lukajovanovic.data.model.LoginData
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsApp : Application() {
	companion object {
		lateinit var instance: ShowsApp
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
	}

}