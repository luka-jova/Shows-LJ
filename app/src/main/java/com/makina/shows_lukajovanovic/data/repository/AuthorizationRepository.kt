package com.makina.shows_lukajovanovic.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.*
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthorizationRepository {
	const val LOGIN_DATA = "login_data"
	const val TOKEN_CODE = "TOKEN_CODE"

	private var tokenResponseMutableLiveData = MutableLiveData<TokenResponse>()
	val tokenResponseLiveData: LiveData<TokenResponse>
		get() = tokenResponseMutableLiveData

	private val registerResponseMutableLiveData = MutableLiveData<RegisterResponse>()
	val registerResponseLiveData: LiveData<RegisterResponse>
		get() = registerResponseMutableLiveData

	init {
		val bufToken= ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE).getString(TOKEN_CODE, "") ?: ""
		if(bufToken.isNotEmpty()) {
			tokenResponseMutableLiveData.value = TokenResponse(bufToken, true)
		}
	}

	fun login(username: String, password: String, rememberMe: Boolean) {
		RetrofitClient.apiService?.loginUser(LoginData(username, password))?.enqueue(object: Callback<TokenResponseFromWeb> {
			override fun onFailure(call: Call<TokenResponseFromWeb>, t: Throwable) {
				tokenResponseMutableLiveData.value = TokenResponse(isSuccessful = false)
			}

			override fun onResponse(call: Call<TokenResponseFromWeb>, response: Response<TokenResponseFromWeb>) {
				with(response) {
					val bufToken = body()?.token?.token ?: ""
					if(isSuccessful && bufToken.isNotEmpty()) {
						if(rememberMe) {
							ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
								.edit()
								.putString(TOKEN_CODE, bufToken)
								.apply()
						}
						tokenResponseMutableLiveData.value =
							TokenResponse(
								bufToken,
								isSuccessful = true
							)
					}
					else {
						tokenResponseMutableLiveData.value = TokenResponse(isSuccessful = false)
					}
				}
			}
		})
	}

	fun register(username: String, password: String) {
		RetrofitClient.apiService?.registerUser(LoginData(username, password))?.enqueue(object : Callback<RegisterResponse> {
				override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
					registerResponseMutableLiveData.value = RegisterResponse(isSuccessful = false)
				}

				override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
					registerResponseMutableLiveData.value = RegisterResponse(isSuccessful = response.isSuccessful)
				}
			})
	}

}