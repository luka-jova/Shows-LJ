package com.makina.shows_lukajovanovic.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.LoginData
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthorizationRepository {
	const val LOGIN_DATA = "login_data"
	const val TOKEN_CODE = "TOKEN_CODE"

	private var token: String = ""
	private val tokenMutableLiveData = MutableLiveData<String>()
	val tokenLiveData: LiveData<String>
		get() = tokenMutableLiveData

	init {
		token = ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE).getString(TOKEN_CODE, "") ?: ""
		tokenMutableLiveData.value = token
	}

	fun login(username: String, password: String, context: Context, rememberMe: Boolean) {
		RetrofitClient.apiService?.loginUser(LoginData(username, password))?.enqueue(object: Callback<TokenResponse> {
			override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
				Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
			}

			override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
				if(response.isSuccessful) {
					token = response.body()?.token?.token ?: ""
					if(token.isEmpty()) Toast.makeText(context, "Token is empty", Toast.LENGTH_SHORT).show()
					tokenMutableLiveData.value = token
					if(rememberMe) {
						ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
							.edit()
							.putString(TOKEN_CODE, token)
							.apply()
					}
				}
				else {
					Toast.makeText(context, "Login failed, wrong username or password", Toast.LENGTH_SHORT).show()
				}
			}
		})
	}
}