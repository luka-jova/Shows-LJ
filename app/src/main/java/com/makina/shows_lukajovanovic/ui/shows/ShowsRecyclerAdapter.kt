package com.makina.shows_lukajovanovic.ui.shows

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_show.view.*
import androidx.core.content.ContextCompat.startActivity
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Show
import com.makina.shows_lukajovanovic.data.network.RetrofitClient
import com.squareup.picasso.Picasso


class ShowsRecyclerAdapter(val startEpisodesFragment: (String, String) -> Unit) : RecyclerView.Adapter<ShowsRecyclerAdapter.ViewHolder>() {
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

	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		fun bind(show: Show, position: Int) {
			with(itemView) {
				if(show.imageId != -1) imageViewShow.setImageResource(show.imageId)
				else {
					Log.d("tigar", show.imageUrl)
					Picasso.get().load(RetrofitClient.BASE_URL + show.imageUrl)
						.placeholder(R.drawable.ic_logo_mark).error(android.R.drawable.stat_notify_error)
						.into(imageViewShow)
				}
				textViewShowName.text = show.name
				textViewShowDate.text = show.airDate

				val cont = context
				setOnClickListener {
					startEpisodesFragment(showsList[ position ].showId, showsList[ position ].name)
				}
			}
		}
	}
}