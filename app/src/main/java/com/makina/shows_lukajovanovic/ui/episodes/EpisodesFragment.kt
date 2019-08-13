package com.makina.shows_lukajovanovic.ui.episodes

import android.graphics.Typeface
import android.os.Bundle
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
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.data.repository.EpisodesRepository
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.add.AddEpisodeFragment
import kotlinx.android.synthetic.main.fragment_episodes.*

class EpisodesFragment: Fragment() {
	companion object {
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		const val TITLE_CODE = "TITLE_CODE"
		const val EPISODES_FRAGMENT_TAG = "EPISODES_FRAGMENT_TAG"
		const val LIKES_NUMBER_CODE = "LIKES_NUMBER_CODE"

		fun newInstance(showId: String, title: String, likesNumber: Int): EpisodesFragment {
			return EpisodesFragment().apply {
				arguments = Bundle().apply {
					putString(TITLE_CODE, title)
					putString(SHOW_ID_CODE, showId)
					putInt(LIKES_NUMBER_CODE, likesNumber)
				}
			}
		}
	}
	private var showId = ""
	private var likesNumber = 0
	private lateinit var viewModel: EpisodesViewModel
	private lateinit var adapter: EpisodesRecyclerAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_episodes, container, false)
	}

	interface EpisodesFragmentContainer {
		fun startAddEpisodeFragment(showId: String)
		fun startEpisodeDetailsFragment()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showId = arguments?.getString(SHOW_ID_CODE) ?: ""
		likesNumber = arguments?.getInt(LIKES_NUMBER_CODE) ?: 0
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

		adapter = EpisodesRecyclerAdapter()
		recyclerViewEpisodes.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewEpisodes.adapter = adapter

		fab.setOnClickListener {
			try {
				(activity as EpisodesFragmentContainer).startAddEpisodeFragment(showId)
			} catch (e: ClassCastException) {
				e.printStackTrace()
			}
		}
		viewModel.episodesFragmentResponseLiveData.observe(this, Observer {response ->
			updateUI(response)
		})
		updateUI(null)
		viewModel.getData()
	}

	private fun updateUI(response: EpisodesFragmentResponse?) {
		adapter.setData(response?.episodesList ?: listOf(), response?.show?.showDescription ?: "")
		textViewLikesCount.text = likesNumber.toString()
		textViewLikesCount.setTypeface(textViewLikesCount.typeface, Typeface.BOLD)
		updateVisibility()
		if(response?.status == ResponseStatus.FAIL) {
			Toast.makeText(requireContext(), "Download error", Toast.LENGTH_SHORT).show()
		}
	}

	private fun updateVisibility() {
		if(viewModel.episodesFragmentResponse?.status == ResponseStatus.DOWNLOADING) {
			progressBarDownloading.visibility = View.VISIBLE
		}
		else {
			progressBarDownloading.visibility = View.INVISIBLE
		}
	}
}