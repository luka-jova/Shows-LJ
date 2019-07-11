package com.makina.shows_lukajovanovic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val username = intent.getStringExtra(USERNAME_CODE)
        textViewWelcomeUser.text = "Welcome, $username"
    }
}
