package com.makina.shows_lukajovanovic.ui.episodes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.shows.ShowsActivity
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeActivity

import kotlinx.android.synthetic.main.activity_episodes.*

class EpisodesActivity : AppCompatActivity() {
	companion object {
		const val SHOW_ID = "SHOW_INDEX"

		fun newInstance(context: Context, showId: Int) : Intent {
			val intent = Intent(context, EpisodesActivity::class.java)
			intent.putExtra(SHOW_ID, showId)
			return intent
		}
	}
	var showId = -1
	private lateinit var viewModel: EpisodesViewModel
	private lateinit var adapter: EpisodesRecyclerAdapter

	fun updateVisibility() {
		if(viewModel.episdesList.isNotEmpty()) {
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
		showId = intent.getIntExtra(SHOW_ID, -1)
		Log.d("tigar", "otvoren s showId: $showId")

		viewModel = ViewModelProviders.of(this).get(EpisodesViewModel::class.java)
		//TODO KAKO DA OVO PRENESEM FIKSNO
		viewModel.showId = showId
		viewModel.initialize()


		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.title = "IME"
		textViewDescription.text = "Description"

		adapter = EpisodesRecyclerAdapter()
		recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)
		recyclerViewEpisodes.adapter = adapter

		fab.setOnClickListener { view ->
			startActivityForResult(AddEpisodeActivity.newInstance(this), 1)
		}

		viewModel.episodesLiveData.observe(this, Observer {episodesList ->
			adapter.setData(episodesList)
			this@EpisodesActivity.updateVisibility()
		})
		updateVisibility()

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if(resultCode == RESULT_OK) {
			val curEpisode: Episode = data?.getSerializableExtra(AddEpisodeActivity.EPISODE_CODE) as Episode
			viewModel.addEpisode(curEpisode)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if(item.itemId == android.R.id.home) {
			finish()
		}
		return super.onOptionsItemSelected(item)
	}


}
