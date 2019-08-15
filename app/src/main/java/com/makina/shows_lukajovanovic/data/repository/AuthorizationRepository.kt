package com.makina.shows_lukajovanovic.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.*
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
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

	private val registrationResponseMutableLiveData = MutableLiveData<RegistrationResponse>()
	val registrationResponseLiveData: LiveData<RegistrationResponse>
		get() = registrationResponseMutableLiveData

	init {
		val bufToken= ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE).getString(TOKEN_CODE, "") ?: ""
		if(bufToken.isNotEmpty()) {
			tokenResponseMutableLiveData.value = TokenResponse(bufToken, status = ResponseStatus.SUCCESS)
		}
	}

	var listener: RepositoryInfoHandler? = null

	var callLogin: Call<TokenResponseFromWeb>? = null

	fun login(username: String, password: String, rememberMe: Boolean) {
		if(tokenResponseLiveData.value?.status == ResponseStatus.DOWNLOADING) return

		tokenResponseMutableLiveData.value = TokenResponse(status = ResponseStatus.DOWNLOADING)
		callLogin = RetrofitClient.apiService?.loginUser(LoginData(username, password))
		callLogin?.enqueue(object: Callback<TokenResponseFromWeb> {
			override fun onFailure(call: Call<TokenResponseFromWeb>, t: Throwable) {
				tokenResponseMutableLiveData.value = TokenResponse(status = ResponseStatus.FAIL)
				if(!call.isCanceled) listener?.displayMessage("Error", "Connection error.")
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
								status = ResponseStatus.SUCCESS
							)
					}
					else {
						tokenResponseMutableLiveData.value = TokenResponse(status = ResponseStatus.FAIL)
						listener?.displayMessage("Error", "Authorization failed")
					}
				}
			}
		})
	}

	var callRegister: Call<CreateAccountResponse>? = null

	fun register(username: String, password: String) {
		if(registrationResponseLiveData.value?.status == ResponseStatus.DOWNLOADING) return

		registrationResponseMutableLiveData.value = RegistrationResponse(status = ResponseStatus.DOWNLOADING)
		callRegister = RetrofitClient.apiService?.registerUser(LoginData(username, password))
		callRegister?.enqueue(object : Callback<CreateAccountResponse> {
				override fun onFailure(call: Call<CreateAccountResponse>, t: Throwable) {
					registrationResponseMutableLiveData.value = RegistrationResponse(status = ResponseStatus.FAIL)
					if(!call.isCanceled) listener?.displayMessage("Error", "Connection error")
				}

				override fun onResponse(call: Call<CreateAccountResponse>, response: Response<CreateAccountResponse>) {
					if(!response.isSuccessful) {
						registrationResponseMutableLiveData.value = RegistrationResponse(status = ResponseStatus.FAIL)
						listener?.displayMessage("Error", "Failed to create account")
					} else {
						loginAfterRegistration(username, password)
					}
				}
			})
	}

	private fun loginAfterRegistration(username: String, password: String) {
		callLogin = RetrofitClient.apiService?.loginUser(LoginData(username, password))
		callLogin?.enqueue(object: Callback<TokenResponseFromWeb> {
			override fun onFailure(call: Call<TokenResponseFromWeb>, t: Throwable) {
				registrationResponseMutableLiveData.value = RegistrationResponse(status = ResponseStatus.FAIL)
				if(!call.isCanceled) listener?.displayMessage("Error", "Connection error")
			}

			override fun onResponse(call: Call<TokenResponseFromWeb>, response: Response<TokenResponseFromWeb>) {
				with(response) {
					val bufToken = body()?.token?.token ?: ""
					if(isSuccessful && bufToken.isNotEmpty()) {
						registrationResponseMutableLiveData.value =
										RegistrationResponse(token = bufToken, status = ResponseStatus.SUCCESS)
					}
					else {
						registrationResponseMutableLiveData.value = RegistrationResponse(status = ResponseStatus.FAIL)
						listener?.displayMessage("Error", "Failed to login in new account")
					}
				}
			}
		})
	}

	fun logout() {
		ShowsApp.instance.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
			.edit()
			.putString(TOKEN_CODE, "")
			.apply()
		tokenResponseMutableLiveData.value =
			TokenResponse(
				"",
				status = ResponseStatus.SUCCESS
			)
	}

	fun cancelCalls() {
		callLogin?.cancel()
		callRegister?.cancel()
	}
}