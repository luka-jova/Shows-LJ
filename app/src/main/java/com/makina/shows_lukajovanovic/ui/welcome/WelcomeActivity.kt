package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makina.shows_lukajovanovic.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME_CODE = "USERNAME"

        fun newInstance(context: Context, username: String) : Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME_CODE, username)
            return intent
        }
        ///TODO HANDLER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val username = intent.getStringExtra(USERNAME_CODE)
        textViewWelcomeUser.text = "Welcome, $username"

    }
}
