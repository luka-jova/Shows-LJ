package com.makina.shows_lukajovanovic.ui.shows

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_shows.*

class ShowsFragment :Fragment() {
	companion object {
		const val FRAGMENT_NAME = "Shows_FRAGMENT"
	}
	private lateinit var adapter: ShowsRecyclerAdapter
	private lateinit var viewModel: ShowsViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_shows, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		adapter = ShowsRecyclerAdapter { showId ->
			Log.d("tigar", "pokrecem EpisodesFragment sa showId: $showId unutar lambde")
			fragmentManager?.beginTransaction()?.apply {
				replace((activity as MainContainerActivity)?.mSlaveContainerId, EpisodesFragment(showId))
				addToBackStack("Episodes $showId")
				commit()
			}
		}
		recyclerViewShows.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewShows.adapter = adapter

		viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
		viewModel.showsListLiveData.observe(this, Observer {showsList ->
			adapter.setData(showsList)
		})
	}

}