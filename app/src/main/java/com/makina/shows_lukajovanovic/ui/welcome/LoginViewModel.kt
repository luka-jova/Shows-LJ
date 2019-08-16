package com.makina.shows_lukajovanovic.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository

class LoginViewModel: ViewModel(), Observer<TokenResponse> {
	private val tokenResponseMutableLiveData = MutableLiveData<TokenResponse>()
	val tokenResponseLiveData: LiveData<TokenResponse>
		get() = tokenResponseMutableLiveData
	val tokenResponse: TokenResponse?
		get() = tokenResponseLiveData.value

	override fun onChanged(response: TokenResponse?) {
		tokenResponseMutableLiveData.value = response
	}

	init {
		AuthorizationRepository.tokenResponseLiveData.observeForever(this)
	}

	fun login(username: String, password: String, rememberMe: Boolean) {
		AuthorizationRepository.login(username, password, rememberMe)
	}

	fun logout() {
		AuthorizationRepository.logout()
	}

	fun cancelCalls() {
		AuthorizationRepository.cancelCalls()
	}

	fun resetToken() {
		AuthorizationRepository.resetToken()
	}

	override fun onCleared() {
		AuthorizationRepository.tokenResponseLiveData.removeObserver(this)
		AuthorizationRepository.cancelCalls()
	}
}