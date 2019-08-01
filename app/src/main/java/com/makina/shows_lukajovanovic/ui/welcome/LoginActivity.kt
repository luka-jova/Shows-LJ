package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.TokenResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_login.*

const val EMAIL_REGEX = """^[A-Za-z][A-Za-z0-9._]*@{1}[A-Za-z0-9._]{1,}\.[A-Za-z0-9._]{1,}"""
//const val EMAIL_REGEX = "."
class LoginActivity : AppCompatActivity() {
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var viewModel: AuthorizationViewModel
    //TODO ovdje i u RegisterActivity popraviti da ako je failed login i user rotira ekran, da se ne pokazuje stalno poruka login failed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin.isEnabled = false
        progressBarDownloading.visibility = View.INVISIBLE

        viewModel = ViewModelProviders.of(this).get(AuthorizationViewModel::class.java)
        viewModel.tokenResponseLiveData.observe(this, Observer {response ->
            updateUI(response)
        })

        buttonLogin.setOnClickListener {
            val usernameInput:String = editTextUsername.text.toString()
            val passwordInput:String = editTextPassword.text.toString()
            viewModel.login(usernameInput, passwordInput, checkBoxRememberMe.isChecked)
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
        buttonLogin.isEnabled = isEmailValid(editTextUsername.text.toString()) && editTextPassword.text.length >= 1
    }

    fun updateUI(response: TokenResponse) {
        when(response.status) {
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
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                progressBarDownloading.visibility = View.INVISIBLE
                updateButton()
            }
            ResponseStatus.DOWNLOADING -> {
                progressBarDownloading.visibility = View.VISIBLE
                buttonLogin.isEnabled = false
            }
        }
    }


}
