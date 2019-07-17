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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_episodes.*
import kotlinx.android.synthetic.main.activity_shows.*

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

	fun updateVisibility() {
		if(ShowsActivity.showsList[ position ].episodeList.size > 0) {
			defaultLayout.visibility = View.INVISIBLE
			recyclerViewEpisodes?.visibility = View.VISIBLE
		}
		else {
			defaultLayout.visibility = View.VISIBLE
			recyclerViewEpisodes?.visibility = View.INVISIBLE
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
		updateVisibility()

		textViewDescription.text = ShowsActivity.showsList[ position ].showDescription

		val recyclerViewEpisodes = recyclerViewEpisodes
		recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)
		recyclerViewEpisodes.adapter = EpisodesRecyclerAdapter(ShowsActivity.showsList[ position ].episodeList)

		fab.setOnClickListener { view ->
			//TODO sta je request code?
			startActivityForResult(AddEpisodeActivity.newInstance(this), 1)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if(resultCode == RESULT_OK) {
			val curEpisode: Episode = data?.getSerializableExtra(AddEpisodeActivity.EPISODE_CODE) as Episode
			val curEpisodeList = ShowsActivity.showsList[ position ].episodeList
			curEpisodeList.add(curEpisode)

			//TODO promijeni da koristim samo jedan val recyclerViewEpisodes ovdje i u onCreate
			val recyclerViewEpisodes = recyclerViewEpisodes
			recyclerViewEpisodes.adapter?.notifyItemInserted(curEpisodeList.size - 1)
			updateVisibility()
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
