package com.makina.shows_lukajovanovic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.activity_episodes.*

class AddEpisodeActivity : AppCompatActivity() {
	companion object {
		const val EPISODE_NAME = "EPISODE_NAME"
		fun newInstance(context: Context) : Intent {
			val intent = Intent(context, AddEpisodeActivity::class.java)
			return intent
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_episode)
		setSupportActionBar(toolbarAddEpisode)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = "Add episode"
		buttonSave.setOnClickListener {
			val resultIntent = Intent()
			resultIntent.putExtra(EPISODE_NAME, editTextEpisodeName.text.toString())
			setResult(RESULT_OK, resultIntent)
			finish()
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}

}
