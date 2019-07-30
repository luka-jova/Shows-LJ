package com.makina.shows_lukajovanovic.ui.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.EpisodesFragmentResponse
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeFragment
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment(): Fragment() {
	companion object {
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		const val TITLE_CODE = "TITLE_CODE"
		const val EPISODES_FRAGMENT_TAG = "EPISODES_FRAGMENT_TAG"

		fun newInstance(showId: String, title: String): EpisodesFragment {
			return EpisodesFragment().apply {
				arguments = Bundle().apply {
					putString(TITLE_CODE, title)
					putString(SHOW_ID_CODE, showId)
				}
			}
		}
	}
	private var showId = ""
	private lateinit var viewModel: EpisodesViewModel
	private lateinit var adapter: EpisodesRecyclerAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_episodes, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showId = arguments?.getString(SHOW_ID_CODE, "") ?: ""
		viewModel = ViewModelProviders.of(this, object: ViewModelProvider.Factory {
			override fun <T : ViewModel?> create(modelClass: Class<T>): T {
				return EpisodesViewModel(showId) as T
			}
		}).get(EpisodesViewModel::class.java)


		toolbarEpisodes.title = arguments?.getString(TITLE_CODE, "") ?: ""
		toolbarEpisodes.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarEpisodes.setNavigationOnClickListener {
			activity?.onBackPressed()
		}
		textViewDescription.text = ""

		adapter = EpisodesRecyclerAdapter()
		recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewEpisodes.adapter = adapter

		fab.setOnClickListener {
			fragmentManager?.beginTransaction()?.apply {
				replace(
					(activity as MainContainerActivity).slaveContainerId,
					AddEpisodeFragment.newInstance(showId),
					AddEpisodeFragment.ADD_EPISODE_TAG
				)
				addToBackStack("AddEpisodeFragment")
				commit()
			}
		}
		viewModel.episodesFragmentResponseLiveData.observe(this, Observer {response ->
			updateUI(response)
		})
		updateVisibility()
		viewModel.getData()
	}

	private fun updateUI(response: EpisodesFragmentResponse?) {
		textViewDescription.text = response?.show?.showDescription ?: ""
		adapter.setData(response?.episodesList ?: listOf())
		updateVisibility()
		if(response?.isSuccessful == false) {
			Toast.makeText(requireContext(), "Download error", Toast.LENGTH_SHORT).show()
		}
	}

	private fun updateVisibility() {
		if(viewModel.episodesFragmentResponse?.episodesList?.isNotEmpty() == true) {
			defaultLayout.visibility = View.INVISIBLE
			recyclerViewEpisodes?.visibility = View.VISIBLE
		}
		else {
			defaultLayout.visibility = View.VISIBLE
			recyclerViewEpisodes?.visibility = View.INVISIBLE
		}
	}
}