package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ShowsApp
import com.makina.shows_lukajovanovic.data.model.LoginData
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.network.Api
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val EMAIL_REGEX = """^[A-Za-z][A-Za-z0-9._]*@{1}[A-Za-z0-9._]{1,}\.[A-Za-z0-9._]{1,}"""
//const val EMAIL_REGEX = "."
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.isEnabled = false

        buttonLogin.setOnClickListener {
            val usernameInput:String = editTextUsername.text.toString()
            val passwordInput:String = editTextPassword.text.toString()

            ShowsApp.login(
                usernameInput,
                passwordInput,
                this)
                {
                    startActivity(MainContainerActivity.newInstance(this))
                }
        }


        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton()
                if(!isEmailValid(editTextUsername.text.toString())) {
                    textInputLayoutUsername.error = getString(R.string.err_username)
                }
                else {
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
            startActivity(RegisterActivity.newInstance(this))
        }

    }

    fun isEmailValid(email : String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }

    fun updateButton() {
        buttonLogin.isEnabled = isEmailValid(editTextUsername.text.toString()) && editTextPassword.text.length >= 8
    }


}
