package com.makina.shows_lukajovanovic.ui.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_show.view.*
import androidx.core.content.ContextCompat.startActivity
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesActivity


class ShowsRecyclerAdapter() : RecyclerView.Adapter<ShowsRecyclerAdapter.ViewHolder>() {
	private var showsList = listOf<Show>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_show, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return showsList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(showsList[ position ], position)

	}

	fun setData(showsList: List<Show>) {
		this.showsList = showsList
		notifyDataSetChanged()
	}

	class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		fun bind(show: Show, position: Int) {
			with(itemView) {
				imageViewShow.setImageResource(show.imageId)
				textViewShowName.text = show.name
				textViewShowDate.text = show.airDate

				val cont = context
				setOnClickListener {
					startActivity(cont, EpisodesActivity.newInstance(cont, position), null)
				}
			}
		}
	}
}