package com.makina.shows_lukajovanovic.ui.episodes.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.Comment
import kotlinx.android.synthetic.main.layout_comment.view.*

class CommentsRecyclerAdapter: RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>() {
	private var commentsList = mutableListOf<Comment>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_comment, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return commentsList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(commentsList[ position ], position)
	}

	fun setData(commentsList: MutableList<Comment>) {
		this.commentsList = commentsList
		notifyDataSetChanged()
	}

	inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		fun bind(comment: Comment, position: Int) {
			with(itemView) {
				textViewEmail.text = comment.userEmail
				textViewComment.text = comment.text
			}
		}
	}
}