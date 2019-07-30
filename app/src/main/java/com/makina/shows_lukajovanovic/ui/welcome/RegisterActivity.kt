package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.LoginData
import com.makina.shows_lukajovanovic.data.model.RegisterResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.network.RetrofitClient

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTextPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
	companion object {
		fun newInstance(context: Context) : Intent {
			return Intent(context, RegisterActivity::class.java)
		}
	}

	private lateinit var viewModel: AuthorizationViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)

		toolbarRegister.title = "Registration"
		toolbarRegister.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarRegister.setNavigationOnClickListener { onBackPressed() }

		viewModel = ViewModelProviders.of(this).get(AuthorizationViewModel::class.java)
		viewModel.tokenResponseLiveData.observe(this, Observer {response ->
			updateUI()
			if(response.status == ResponseStatus.SUCCESS) {
				if(response.token.isNotEmpty()) {
					val bufIntent = WelcomeActivity.newInstance(this@RegisterActivity, editTextEmail.text.toString())
					bufIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
					startActivity(bufIntent)
				}
			}
			if(response.status == ResponseStatus.FAIL) {
				Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
			}
		})

		//TODO nakon registera nemoj da se mogu mijenjati podaci
		viewModel.registerResponseLiveData.observe(this, Observer {response ->
			updateUI()
			if(response.status == ResponseStatus.SUCCESS) {
				Toast.makeText(this, "Successful registration", Toast.LENGTH_SHORT).show()
				viewModel.login(editTextEmail.text.toString(), editTextPassword.text.toString(), false)
			}
			if(response.status == ResponseStatus.FAIL) {
				Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
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
	}

	private fun updateUI() {
		if(viewModel.tokenResponse?.status == ResponseStatus.DOWNLOADING
			|| viewModel.registerResponse?.status == ResponseStatus.DOWNLOADING) {
			buttonRegister.isEnabled = false
			progressBarDownloading.visibility = View.VISIBLE
		}
		else {
			buttonRegister.isEnabled = true
			progressBarDownloading.visibility = View.INVISIBLE
		}
	}
}