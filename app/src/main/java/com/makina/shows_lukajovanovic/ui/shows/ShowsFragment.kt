package com.makina.shows_lukajovanovic.ui.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.ShowsRepository
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment.Companion.EPISODES_FRAGMENT_TAG
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment.Companion.SHOW_ID_CODE
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_shows.*
import java.lang.reflect.Array.setInt

class ShowsFragment :Fragment() {
	companion object {
		const val FRAGMENT_NAME = "Shows_FRAGMENT"
	}
	//TODO handle error messages for this and other Fragments
	private lateinit var adapter: ShowsRecyclerAdapter
	private lateinit var viewModel: ShowsViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_shows, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		adapter = ShowsRecyclerAdapter { showId, title ->
			if(fragmentManager?.findFragmentByTag(EPISODES_FRAGMENT_TAG) != null) fragmentManager?.popBackStack()
			fragmentManager?.beginTransaction()?.apply {
				replace(
					(activity as MainContainerActivity)?.slaveContainerId,
					EpisodesFragment.newInstance(showId, title),
					EpisodesFragment.EPISODES_FRAGMENT_TAG
				)
				addToBackStack("Episodes $showId")
				commit()
			}
		}
		recyclerViewShows.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewShows.adapter = adapter

		viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
		viewModel.showsListResponseLiveData.observe(this, Observer {showsListResponse ->
			updateUI(showsListResponse)
		})
		progressBarDownloading.visibility = View.INVISIBLE
		viewModel.getShowsList()
	}

	fun updateUI(response: ShowsListResponse?) {
		when(response?.status) {
			ResponseStatus.SUCCESS -> {
				adapter.setData(response.showsList ?: listOf())
				progressBarDownloading.visibility = View.INVISIBLE
			}
			ResponseStatus.DOWNLOADING -> {
				progressBarDownloading.visibility = View.VISIBLE
			}
			ResponseStatus.FAIL -> {
				progressBarDownloading.visibility = View.INVISIBLE
				Toast.makeText(requireContext(), "Downloading failed", Toast.LENGTH_SHORT).show()
			}
		}
	}

}