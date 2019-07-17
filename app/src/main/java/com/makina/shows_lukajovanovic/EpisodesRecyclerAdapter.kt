package com.makina.shows_lukajovanovic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_episode.view.*
import kotlinx.android.synthetic.main.layout_show.view.*

class EpisodesRecyclerAdapter(val episodeList: MutableList<Episode>) : RecyclerView.Adapter<EpisodesRecyclerAdapter.ViewHolder>() {
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


	class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}