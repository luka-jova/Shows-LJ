package com.makina.shows_lukajovanovic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_episodes.*

class EpisodesActivity : AppCompatActivity() {
	companion object {
		const val SHOW_INDEX = "SHOW_INDEX"

		fun newInstance(context: Context, showIndex: Int) : Intent {
			val intent = Intent(context, EpisodesActivity::class.java)
			intent.putExtra(SHOW_INDEX, showIndex)
			return intent
		}
	}
	var position = -1

	fun generateText() {
		textViewEpisodesContent.text = ""
		if(ShowsActivity.showsList[ position ].episodeList.size > 0) {
			defaultLayout.visibility = View.INVISIBLE
			textViewEpisodesContent.text = ""
			var i = 0
			for(cur in ShowsActivity.showsList[ position ].episodeList)
				textViewEpisodesContent.text =
					textViewEpisodesContent.text.toString() + "${++i}. $cur\n\n"
		}
		else {
			defaultLayout.visibility = View.VISIBLE
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_episodes)
		setSupportActionBar(toolbarEpisodes)
		position = intent.getIntExtra(SHOW_INDEX, 0)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = ShowsActivity.showsList[ position ].name
		generateText()


		fab.setOnClickListener { view ->
			//TODO sta je request code?
			startActivityForResult(AddEpisodeActivity.newInstance(this), 1)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if(resultCode == RESULT_OK) {
			//TODO jel ovo radi: ako je null postavi na default
			val episodeName: String = data?.getStringExtra(AddEpisodeActivity.EPISODE_NAME) ?: "Default"
			ShowsActivity.showsList[ position ].episodeList.add(episodeName)
			generateText()
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		///TODO zasto ovo radi? zasto se logovi ne ispisuju kad ja kliknem nego se ispise samo jednom na pocetku?
		if(item.itemId == android.R.id.home) {
			finish()
		}
		Log.d("moj tag", item.itemId.toString())
		return super.onOptionsItemSelected(item)
	}

}
