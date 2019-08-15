package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository
import com.makina.shows_lukajovanovic.data.repository.RepositoryInfoHandler
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.shared.InfoAllertDialog
import kotlinx.android.synthetic.main.activity_login.*

const val EMAIL_REGEX = """^[A-Za-z][A-Za-z0-9._+]*@{1}[A-Za-z0-9._+]{1,}\.[A-Za-z0-9._+]{1,}"""
const val minPasswordCnt = 5

//const val EMAIL_REGEX = "."
class LoginActivity : AppCompatActivity(), RepositoryInfoHandler {
	companion object {
		fun newInstance(context: Context): Intent {
			return Intent(context, LoginActivity::class.java)
		}
	}

	private lateinit var viewModel: LoginViewModel
	private var active = false
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		buttonLogin.isEnabled = false
		progressBarDownloading.visibility = View.INVISIBLE

		AuthorizationRepository.listener = this

		viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
		viewModel.tokenResponseLiveData.observe(this, Observer { response ->
			if (response == null) return@Observer
			updateUI(response)
		})

		buttonLogin.setOnClickListener {
			val usernameInput: String = editTextUsername.text.toString()
			val passwordInput: String = editTextPassword.text.toString()
			viewModel.login(usernameInput, passwordInput, checkBoxRememberMe.isChecked)
		}


		editTextUsername.addTextChangedListener(object : TextWatcher {
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				updateButton()
				if (!isEmailValid(editTextUsername.text.toString())) {
					textInputLayoutUsername.error = getString(R.string.err_username)
				} else {
					textInputLayoutUsername.error = ""
				}
			}

			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
		})

		editTextPassword.addTextChangedListener(object : TextWatcher {
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				updateButton()
			}

			override fun afterTextChanged(p0: Editable?) {
			}

			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
		})

		textViewCreateAccount.setOnClickListener {
			if (viewModel.tokenResponse?.status != ResponseStatus.DOWNLOADING) {
				startActivity(RegisterActivity.newInstance(this))
			}
		}

	}

	fun isEmailValid(email: String): Boolean {
		return EMAIL_REGEX.toRegex().matches(email)
	}

	fun updateButton() {
		buttonLogin.isEnabled = isEmailValid(editTextUsername.text.toString()) && editTextPassword.text.length >= minPasswordCnt
	}

	private fun updateUI(response: TokenResponse) {
		when (response.status) {
			ResponseStatus.SUCCESS -> {
				if (response.token.isNotEmpty()) {
					progressBarDownloading.visibility = View.INVISIBLE
					Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
					startActivity(MainContainerActivity.newInstance(this))
					finish()
				}
				updateButton()
			}
			ResponseStatus.FAIL -> {
				progressBarDownloading.visibility = View.INVISIBLE
				updateButton()
			}
			ResponseStatus.DOWNLOADING -> {
				progressBarDownloading.visibility = View.VISIBLE
				buttonLogin.isEnabled = false
			}
		}
	}

	override fun onResume() {
		super.onResume()
		active = true
	}

	override fun onStop() {
		active = false
		viewModel.cancelCalls()
		super.onStop()
	}

	override fun displayMessage(title: String, message: String) {
		if(active)
			InfoAllertDialog.newInstance(title, message).show(supportFragmentManager, "Message fragment")
	}

}
