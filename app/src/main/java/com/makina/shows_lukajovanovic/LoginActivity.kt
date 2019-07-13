package com.makina.shows_lukajovanovic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.*

const val EMAIL_REGEX = """^[A-Za-z][A-Za-z0-9._]*@{1}[A-Za-z0-9._]{1,}\.[A-Za-z0-9._]{1,}"""

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.isEnabled = false

        buttonLogin.setOnClickListener {
            val usernameInput:String = editTextUsername.text.toString()
            startActivity(WelcomeActivity.newInstance(this, usernameInput))
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

    }

    fun isEmailValid(email : String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }

    fun updateButton() {
        buttonLogin.isEnabled = isEmailValid(editTextUsername.text.toString()) && editTextPassword.text.length >= 8
    }
}
