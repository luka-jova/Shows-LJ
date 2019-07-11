package com.makina.shows_lukajovanovic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
const val USERNAME_CODE = "USERNAME"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("neki tag", "pozvan onCreate()")
        setContentView(R.layout.activity_login)
        buttonLogin.isEnabled = false

        buttonLogin.setOnClickListener {
            //Toast.makeText(this, "Kliknut je gumb", Toast.LENGTH_SHORT).show()
            val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
            val usernameInput = editTextUsername.text
            Log.d("neki tag", "usernameInput = $usernameInput")
            welcomeActivityIntent.putExtra(USERNAME_CODE, "" + usernameInput)
            startActivity(welcomeActivityIntent)
        }
        var passwordVisible = false
        buttonShowPassword.setOnClickListener {
            if(passwordVisible) {
                passwordVisible = false
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else {
                passwordVisible = true
                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }

        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                ///Log.d("neki tag", "afterTextChanged()")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ///Log.d("neki tag", "beforeTextChanged")
            }
        })
        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                ///Log.d("neki tag", "afterTextChanged()")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ///Log.d("neki tag", "beforeTextChanged")
            }
        })

    }

    fun updateButton() {
        if(editTextUsername.text.length >= 1 && editTextPassword.text.length >= 8)
            buttonLogin.isEnabled = true
        else
            buttonLogin.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        val a = 10
        Log.d("neki tag", "pozvan onStart(), probavam $a")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("neki tag", "pozvan onRestart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("neki tag", "pozvan onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("neki tag", "pozvan onDestroy()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("neki tag", "pozvan onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("neki tag", "pozvan onStop()")
    }
}
