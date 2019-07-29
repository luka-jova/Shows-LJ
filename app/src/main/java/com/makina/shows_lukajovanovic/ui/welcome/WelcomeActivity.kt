package com.makina.shows_lukajovanovic.ui.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import java.lang.Exception

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME_CODE = "USERNAME"
        const val START_ACTIVITY_CODE = 0

        fun newInstance(context: Context, username: String) : Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME_CODE, username)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val username = intent.getStringExtra(USERNAME_CODE)
        textViewWelcomeUser.text = "Welcome, $username"

        //TODO popravi handler
        val handlerThread = Handler {
            if(it.what == START_ACTIVITY_CODE)
            startActivity(MainContainerActivity.newInstance(this@WelcomeActivity))
            this@WelcomeActivity.finish()
            true
        }
        handlerThread.sendEmptyMessageDelayed(START_ACTIVITY_CODE, 2000)
    }

    override fun onStop() {
        super.onStop()
        ///TODO ovdje moram ubit handler
    }
}
