package com.makina.shows_lukajovanovic.ui.episodes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.shows.ShowsActivity
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeActivity

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
		/**ShowsActivity.restoreData(applicationContext)*/

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
		recyclerViewEpisodes.adapter =
			EpisodesRecyclerAdapter(ShowsActivity.showsList[position].episodeList)

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
			/**ShowsActivity.saveData(applicationContext)*/
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}


}
