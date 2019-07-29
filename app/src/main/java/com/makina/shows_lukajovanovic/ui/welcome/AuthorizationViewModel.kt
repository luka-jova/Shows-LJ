package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import androidx.lifecycle.*
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository

class AuthorizationViewModel: ViewModel(), Observer<String> {

	private val tokenMutableLiveData = MutableLiveData<String>()
	val tokenLiveData: LiveData<String>
		get() = tokenMutableLiveData
	val token: String
		get() = tokenLiveData.value ?: ""

	init {
		AuthorizationRepository.tokenLiveData.observeForever(this)
	}

	override fun onChanged(t: String?) {
		tokenMutableLiveData.value = (t ?: "")
	}

	fun login(username: String, password: String, context: Context, rememberMe: Boolean) {
		AuthorizationRepository.login(username, password, context, rememberMe)
	}

	override fun onCleared() {
		AuthorizationRepository.tokenLiveData.removeObserver(this)
	}
}