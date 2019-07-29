package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.LoginData
import com.makina.shows_lukajovanovic.data.model.RegisterResponse
import com.makina.shows_lukajovanovic.data.network.Api
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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)

		toolbarRegister.title = "Registration"
		toolbarRegister.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarRegister.setNavigationOnClickListener { onBackPressed() }

		buttonRegister.setOnClickListener {
			if(editTextPassword.text.toString() != editTextRepeatPassword.text.toString()) {
				Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
			}
			else {
				val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
				apiService
					?.registerUser(LoginData(editTextEmail.text.toString(), editTextPassword.text.toString()))
					?.enqueue(object: Callback<RegisterResponse> {
						override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
							Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
						}

						override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
							if(response.isSuccessful) {
								Toast.makeText(this@RegisterActivity, "Successful registration", Toast.LENGTH_SHORT).show()
								ShowsApp.login(
									editTextEmail.text.toString(),
									editTextPassword.text.toString(),
									this@RegisterActivity,
									{
										startActivity(WelcomeActivity.newInstance(this@RegisterActivity, editTextEmail.text.toString()))
										finish()
									}
								)
							}
							else {
								//TODO zasto javi da je onSuccessful == true ako vec postoji account?
								Toast.makeText(this@RegisterActivity, "Change username or password", Toast.LENGTH_SHORT).show()
							}
						}
					})
			}
		}

	}
}