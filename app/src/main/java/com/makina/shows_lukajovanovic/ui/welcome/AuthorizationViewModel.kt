package com.makina.shows_lukajovanovic.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.RegisterResponse
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository

class AuthorizationViewModel: ViewModel() {

	private val registerResponseMutableLiveData = MutableLiveData<RegisterResponse>()
	val registerResponseLiveData: LiveData<RegisterResponse>
		get() = registerResponseMutableLiveData

	val observerRegister = Observer<RegisterResponse> {response ->
		registerResponseMutableLiveData.value = response
	}

	private val tokenResponseMutableLiveData = MutableLiveData<TokenResponse>()
	val tokenResponseLiveData: LiveData<TokenResponse>
		get() = tokenResponseMutableLiveData
	val observerToken = Observer<TokenResponse> {response ->
		tokenResponseMutableLiveData.value = response
	}

	init {
		AuthorizationRepository.registerResponseLiveData.observeForever(observerRegister)
		AuthorizationRepository.tokenResponseLiveData.observeForever(observerToken)
	}

	fun login(username: String, password: String, rememberMe: Boolean) {
		AuthorizationRepository.login(username, password, rememberMe)
	}

	fun register(username: String, password: String) {
		AuthorizationRepository.register(username, password)
	}

	override fun onCleared() {
		AuthorizationRepository.registerResponseLiveData.removeObserver(observerRegister)
		AuthorizationRepository.tokenResponseLiveData.removeObserver(observerToken)
	}
}