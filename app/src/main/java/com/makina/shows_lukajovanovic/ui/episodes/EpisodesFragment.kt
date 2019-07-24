package com.makina.shows_lukajovanovic.ui.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeActivity
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment(): Fragment() {
	companion object {
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		const val EPISODES_FRAGMENT_TAG = "EPISODES_FRAGMENT_TAG"
	}
	private var showId = -1
	private lateinit var viewModel: EpisodesViewModel
	private lateinit var adapter: EpisodesRecyclerAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_episodes, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		(activity as MainContainerActivity)?.updateVisibility()
		//TODO gornja linija
		showId = arguments?.getInt(SHOW_ID_CODE, -1) ?: -1
		Log.d("tigar", "EpisodesFragment $showId, onViewCreated")
		viewModel = ViewModelProviders.of(this, object: ViewModelProvider.Factory {
			override fun <T : ViewModel?> create(modelClass: Class<T>): T {
				return EpisodesViewModel(showId) as T
			}
		}).get(EpisodesViewModel::class.java)


		toolbarEpisodes.title = "Ime"
		toolbarEpisodes.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarEpisodes.setNavigationOnClickListener {
			activity?.onBackPressed()
		}
		textViewDescription.text = "Description"

		viewModel.showLiveData.observe(this, Observer {show ->
			toolbarEpisodes.title = show.name
			textViewDescription.text = show.showDescription
		})

		adapter = EpisodesRecyclerAdapter()
		recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewEpisodes.adapter = adapter

		fab.setOnClickListener { view ->
			startActivityForResult(AddEpisodeActivity.newInstance(requireContext(), showId), 1)
		}

		viewModel.episodesLiveData.observe(this, Observer {episodesList ->
			adapter.setData(episodesList)
			this@EpisodesFragment.updateVisibility()
		})
		updateVisibility()
	}

	fun updateVisibility() {
		//TODO jel ok da ovdje pristupam podacima u viewModelu?
		if(viewModel.episodesList.isNotEmpty()) {
			defaultLayout.visibility = View.INVISIBLE
			recyclerViewEpisodes?.visibility = View.VISIBLE
		}
		else {
			defaultLayout.visibility = View.VISIBLE
			recyclerViewEpisodes?.visibility = View.INVISIBLE
		}
	}

}