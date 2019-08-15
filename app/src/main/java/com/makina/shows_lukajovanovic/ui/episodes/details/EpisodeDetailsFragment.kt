package com.makina.shows_lukajovanovic.ui.episodes.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode_details.*

class EpisodeDetailsFragment: Fragment() {
	companion object {
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		const val EPISODE_ID_CODE = "EPISODE_ID_CODE"
		const val EPISODE_DETAILS_FRAGMENT_TAG = "EPISODE_DETAILS_FRAGMENT_TAG"

		fun newInstance(showId: String, episodeId: String): EpisodeDetailsFragment {
			return EpisodeDetailsFragment().apply {
				arguments = Bundle().apply {
					putString(SHOW_ID_CODE, showId)
					putString(EPISODE_ID_CODE, episodeId)
				}
			}
		}
	}

	private var showId = ""
	private var episodeId = ""
	private lateinit var viewModel: EpisodeDetailsViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_episode_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showId = arguments?.getString(SHOW_ID_CODE) ?: ""
		episodeId = arguments?.getString(EPISODE_ID_CODE) ?: ""

		viewModel = ViewModelProviders.of(this).get(EpisodeDetailsViewModel::class.java)
		viewModel.episodeDetailsResponseLiveData.observe(this, Observer {
			updateUI()
		})

		viewModel.setupEpisode(showId, episodeId)

		toolbarEpisodeDetails.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
		toolbarEpisodeDetails.setNavigationOnClickListener {
			activity?.onBackPressed()
		}

		buttonComments.setOnClickListener {
			try {
				(activity as EpisodeDetailsFragmentContainer).startEpisodeComments(showId, episodeId)
			} catch (e: ClassCastException) {
				e.printStackTrace()
			}
		}

	}

	interface EpisodeDetailsFragmentContainer {
		fun startEpisodeComments(showId: String, episodeId: String)
	}

	private fun updateUI() {
		val response = viewModel.episodeDetailsResponseLiveData.value
		if(response == null) {Log.d("tigar", "null je response"); return}
		val episode = response.episode
		textViewEpisodeDescription.text = episode.episodeDescription
		textViewEpisodeName.text = episode.name
		textViewSE.text = "S${episode.seasonNum} E${episode.episodeNum}"
		if(episode.imageUrl.isNotEmpty()) {
			Picasso.get().load(RetrofitClient.BASE_URL + episode.imageUrl)
				.into(imageViewEpisode)
		}
	}

}