package com.makina.shows_lukajovanovic.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import kotlinx.android.synthetic.main.layout_episode.view.*

class EpisodesRecyclerAdapter() : RecyclerView.Adapter<EpisodesRecyclerAdapter.ViewHolder>() {
	var episodeList: List<Episode> = listOf()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_episode, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return episodeList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val curEpisode = episodeList[ position ]

		holder.itemView.textViewEpisode.text = curEpisode.name
		holder.itemView.textViewEpisodeSENumber.text =
			"S${curEpisode.seasonNum.toString().padStart(2, '0')} E${curEpisode.episodeNum.toString().padStart(2, '0')}"
	}

	fun setData(episodeList: List<Episode>) {
		this.episodeList = episodeList
		notifyDataSetChanged()
	}

	class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}