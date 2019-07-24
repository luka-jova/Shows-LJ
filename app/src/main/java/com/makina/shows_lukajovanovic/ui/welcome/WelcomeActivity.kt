package com.makina.shows_lukajovanovic.ui.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*
import java.lang.Exception

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME_CODE = "USERNAME"

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
        object: AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                try {
                    Thread.sleep(2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onPostExecute(result: Unit?) {
                startActivity(MainContainerActivity.newInstance(this@WelcomeActivity))
                this@WelcomeActivity.finish()
            }

        }.execute()

    }
}
