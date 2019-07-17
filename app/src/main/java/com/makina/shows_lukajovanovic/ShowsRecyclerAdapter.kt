package com.makina.shows_lukajovanovic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_show.view.*
import androidx.core.content.ContextCompat.startActivity


class ShowsRecyclerAdapter(val showsList: MutableList<Show>) : RecyclerView.Adapter<ShowsRecyclerAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_show, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return showsList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val imageViewShow = holder.itemView.imageViewShow
		val textViewShowName = holder.itemView.textViewShowName
		val textViewShowDate = holder.itemView.textViewShowDate

		imageViewShow.setImageResource(showsList[ position ].imageId)
		textViewShowDate.text = showsList[ position ].airDate
		textViewShowName.text = showsList[ position ].name

		val cont = holder.itemView.context
		holder.itemView.setOnClickListener {
			///TODO ZASTO OVDJE NE PREPOZNAJE SAMO startActivity(EpisodesActivity.newInstance....)??
			startActivity(cont, EpisodesActivity.newInstance(cont, position), null)
		}
	}


	class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

}