package com.makina.shows_lukajovanovic.ui.episodes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Episode
import kotlinx.android.synthetic.main.fragment_episodes_header.view.*
import kotlinx.android.synthetic.main.layout_episode.view.*
import java.io.FileDescriptor

class EpisodesRecyclerAdapter: RecyclerView.Adapter<EpisodesRecyclerAdapter.ViewHolder>() {
	companion object {
		const val CODE_HEADER = 0
		const val CODE_EPISODE = 1
	}
	var episodeList: List<Episode> = listOf()
	var episodeDescription: String = ""

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view =
			when(viewType) {
				CODE_EPISODE -> LayoutInflater.from(parent.context).inflate(R.layout.layout_episode, parent, false)
				else -> LayoutInflater.from(parent.context).inflate(R.layout.fragment_episodes_header, parent, false)
			}
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return episodeList.size + 1
	}

	override fun getItemViewType(position: Int): Int {
		return when (position) {
			0 -> CODE_HEADER
			else -> CODE_EPISODE
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if(getItemViewType(position) == CODE_HEADER) {
			holder.itemView.textViewDescription.text = episodeDescription
			holder.itemView.defaultLayout.visibility = if(episodeList.isEmpty()) View.VISIBLE else View.GONE
			return
		}
		val curEpisode = episodeList[ position - 1 ]
		holder.itemView.textViewEpisode.text = curEpisode.name
		holder.itemView.textViewEpisodeSENumber.text =
			"S${curEpisode.seasonNum.toString().padStart(2, '0')} E${curEpisode.episodeNum.toString().padStart(2, '0')}"
	}

	fun setData(episodeList: List<Episode>, episodeDescription: String) {
		this.episodeList = episodeList
		this.episodeDescription = episodeDescription
		notifyDataSetChanged()
	}

	class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
}