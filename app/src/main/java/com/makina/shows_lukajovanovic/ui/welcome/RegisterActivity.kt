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
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.AuthorizationRepository
import com.makina.shows_lukajovanovic.data.repository.RepositoryInfoHandler
import com.makina.shows_lukajovanovic.ui.shared.InfoAllertDialog

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTextPassword

class RegisterActivity : AppCompatActivity(), RepositoryInfoHandler {
	companion object {
		fun newInstance(context: Context) : Intent {
			return Intent(context, RegisterActivity::class.java)
		}
	}

	private lateinit var viewModel: RegisterViewModel
	private var active = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)

		toolbarRegister.title = "Registration"
		toolbarRegister.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarRegister.setNavigationOnClickListener { onBackPressed() }

		AuthorizationRepository.listener = this

		viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
		viewModel.registrationResponseLiveData.observe(this, Observer {response ->
			updateUI()
			if(response == null) return@Observer
			if(response.status == ResponseStatus.SUCCESS) {
				Toast.makeText(this, "Successful registration", Toast.LENGTH_SHORT).show()
				val bufIntent = WelcomeActivity.newInstance(this@RegisterActivity, editTextEmail.text.toString())
				bufIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
				startActivity(bufIntent)
			}
		})

		buttonRegister.setOnClickListener {
			if(editTextPassword.text.toString() != editTextRepeatPassword.text.toString()) {
				Toast.makeText(this, "Please repeat correct password", Toast.LENGTH_SHORT).show()
			}
			else {
				viewModel.register(editTextEmail.text.toString(), editTextPassword.text.toString())
			}
		}
		updateUI()

		editTextEmail.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {}
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				updateUI()
			}
		})
		editTextPassword.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {}
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				updateUI()
			}
		})
		editTextRepeatPassword.addTextChangedListener(object: TextWatcher {
			override fun afterTextChanged(p0: Editable?) {}
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				updateUI()
			}
		})
	}

	private fun updateUI() {
		isFormValid()
		if(viewModel.registerResponse?.status == ResponseStatus.DOWNLOADING) {
			buttonRegister.isEnabled = false
			progressBarDownloading.visibility = View.VISIBLE
			editTextEmail.isEnabled = false
			editTextPassword.isEnabled = false
			editTextRepeatPassword.isEnabled = false
		}
		else {
			buttonRegister.isEnabled = isFormValid()
			progressBarDownloading.visibility = View.INVISIBLE
			editTextEmail.isEnabled = true
			editTextPassword.isEnabled = true
			editTextRepeatPassword.isEnabled = true
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

	private fun isFormValid(): Boolean {
		if (!isEmailValid(editTextEmail.text.toString())) {
			textInputLayoutEmail.error = getString(R.string.err_username)
		} else {
			textInputLayoutEmail.error = ""
		}
		if(editTextPassword.text.length < minPasswordCnt) {
			textInputLayoutPassword.error = getString(R.string.err_password)
		} else {
			textInputLayoutPassword.error = ""
		}

		if(editTextRepeatPassword.text.toString() != editTextPassword.text.toString()) {
			textInputLayoutRepeatPassword.error = "Repeat correct password"
		} else {
			textInputLayoutRepeatPassword.error = ""
		}

		return isEmailValid(editTextEmail.text.toString()) && editTextPassword.text.toString().length >= minPasswordCnt && editTextRepeatPassword.text.toString().length >= minPasswordCnt
	}

	fun isEmailValid(email: String): Boolean {
		return EMAIL_REGEX.toRegex().matches(email)
	}

}