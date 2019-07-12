package com.makina.shows_lukajovanovic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_login.*

const val USERNAME_CODE = "USERNAME"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.isEnabled = false

        buttonLogin.setOnClickListener {
            val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
            val usernameInput = editTextUsername.text
            welcomeActivityIntent.putExtra(USERNAME_CODE, usernameInput.toString())
            startActivity(welcomeActivityIntent)
        }


        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton()
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


    fun updateButton() {
        buttonLogin.isEnabled = editTextUsername.text.isNotEmpty() && editTextPassword.text.length >= 8
    }
}
