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
		var token = ""

		fun login(username: String, password: String, context: Context, onSuccess: () -> Unit = {}) {
			val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
			apiService?.loginUser(LoginData(username, password))?.enqueue(object: Callback<TokenResponse> {
				override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
					Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
				}

				override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
					if(response.isSuccessful) {
						Log.d("tigar", response.body()?.token?.token ?: "token prazan")
						token = response.body()?.token?.token ?: ""
						onSuccess()
					}
					else {
						Toast.makeText(context, "Wrong username or password", Toast.LENGTH_SHORT).show()
					}
				}
			})
		}
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
	}

}