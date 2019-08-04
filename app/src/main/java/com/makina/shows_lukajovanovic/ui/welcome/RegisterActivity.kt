package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.network.ResponseStatus

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTextPassword

class RegisterActivity : AppCompatActivity() {
	companion object {
		fun newInstance(context: Context) : Intent {
			return Intent(context, RegisterActivity::class.java)
		}
	}

	private lateinit var viewModel: RegisterViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)

		toolbarRegister.title = "Registration"
		toolbarRegister.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarRegister.setNavigationOnClickListener { onBackPressed() }

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
				viewModel.register(editTextEmail.text.toString(), editTextPassword.text.toString(), ::showInfo)
			}
		}
		updateUI()
	}

	private fun updateUI() {
		if(viewModel.registerResponse?.status == ResponseStatus.DOWNLOADING) {
			buttonRegister.isEnabled = false
			progressBarDownloading.visibility = View.VISIBLE
			editTextEmail.isEnabled = false
			editTextPassword.isEnabled = false
			editTextRepeatPassword.isEnabled = false
		}
		else {
			buttonRegister.isEnabled = true
			progressBarDownloading.visibility = View.INVISIBLE
			editTextEmail.isEnabled = true
			editTextPassword.isEnabled = true
			editTextRepeatPassword.isEnabled = true
		}
	}

	private fun showInfo(messageCode: Int) {
		Toast.makeText(
			this,
			when(messageCode) {
				ResponseStatus.INFO_ERROR_INTERNET -> "Check your internet connection"
				ResponseStatus.INFO_ERROR_LOGIN -> "Login failed"
				ResponseStatus.INFO_ERROR_REGISTER -> "Registration failed"
				else -> ""
			},
			Toast.LENGTH_SHORT
		).show()
	}
}