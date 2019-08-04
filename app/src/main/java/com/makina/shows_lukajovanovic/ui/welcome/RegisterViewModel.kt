package com.makina.shows_lukajovanovic.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.makina.shows_lukajovanovic.data.model.RegistrationResponse
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository

class RegisterViewModel: ViewModel(), Observer<RegistrationResponse> {
	private val registrationResponseMutableLiveData = MutableLiveData<RegistrationResponse>()
	val registrationResponseLiveData: LiveData<RegistrationResponse>
		get() = registrationResponseMutableLiveData
	val registerResponse: RegistrationResponse?
		get() = registrationResponseLiveData.value

	init {
		AuthorizationRepository.registrationResponseLiveData.observeForever(this)
	}

	override fun onChanged(response: RegistrationResponse?) {
		registrationResponseMutableLiveData.value = response
	}

	fun register(username: String, password: String, showInfo: (Int) -> Unit) {
		AuthorizationRepository.register(username, password, showInfo)
	}

	override fun onCleared() {
		AuthorizationRepository.registrationResponseLiveData.removeObserver(this)
	}

}