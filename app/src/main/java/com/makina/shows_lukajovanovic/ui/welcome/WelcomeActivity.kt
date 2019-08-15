package com.makina.shows_lukajovanovic.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

	companion object {
		const val USERNAME_CODE = "USERNAME"
		const val START_ACTIVITY_CODE = 0

		fun newInstance(context: Context, username: String): Intent {
			val intent = Intent(context, WelcomeActivity::class.java)
			intent.putExtra(USERNAME_CODE, username)
			return intent
		}

	}

	private val handlerThread = Handler()
	private val startActivityRunnable = Runnable {
		startActivity(MainContainerActivity.newInstance(this))
		finish()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_welcome)
		val username = intent.getStringExtra(USERNAME_CODE)
		textViewWelcomeUser.text = "Welcome, $username"
	}

	override fun onResume() {
		super.onResume()
		handlerThread.postDelayed(startActivityRunnable, 2000)
	}

	override fun onStop() {
		handlerThread.removeCallbacks(startActivityRunnable)
		super.onStop()
	}
}
