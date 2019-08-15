package com.makina.shows_lukajovanovic.ui.episodes.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.CommentsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import kotlinx.android.synthetic.main.fragment_comments.*


class CommentsFragment : Fragment() {
	companion object {
		const val COMMENTS_FRAGMENT_CODE = "COMMENTS_FRAGMENT_CODE"
		const val SHOW_ID_CODE = "SHOW_ID_CODE"
		const val EPISODE_ID_CODE = "EPISODE_ID_CODE"

		fun newInstance(showId: String, episodeId: String): CommentsFragment {
			return CommentsFragment().apply {
				arguments = Bundle().apply {
					putString(SHOW_ID_CODE, showId)
					putString(EPISODE_ID_CODE, episodeId)
				}
			}
		}
	}

	private var showId = ""
	private var episodeId = ""
	private lateinit var viewModel: CommentsViewModel
	private lateinit var adapter: CommentsRecyclerAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_comments, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showId = arguments?.getString(SHOW_ID_CODE) ?: ""
		episodeId = arguments?.getString(EPISODE_ID_CODE) ?: ""

		viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
		viewModel.commentsListResponseLiveData.observe(this, Observer { response ->
			updateUI(response)
		})
		viewModel.getComments(showId, episodeId)

		toolbarComments.setNavigationOnClickListener {
			activity?.onBackPressed()
		}

		buttonPost.setOnClickListener {
			if(editTextComment.text.toString().isNotEmpty())
				viewModel.addComment(
					showId,
					episodeId,
					editTextComment.text.toString()
				)
		}

		textInputLayoutComments.showDividers = LinearLayout.SHOW_DIVIDER_BEGINNING

		adapter = CommentsRecyclerAdapter()
		recyclerViewComments.adapter = adapter
		recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
		recyclerViewComments.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

	}

	private fun updateUI(response: CommentsListResponse) {
		if (response.commentsList.isEmpty()) {
			defaultLayout.visibility = View.VISIBLE
			recyclerViewComments.visibility = View.INVISIBLE
		} else {
			defaultLayout.visibility = View.INVISIBLE
			recyclerViewComments.visibility = View.VISIBLE
		}
		adapter.setData(response.commentsList)
		when (response.status) {
			ResponseStatus.SUCCESS -> {
				progressBarDownloading.visibility = View.INVISIBLE
			}
			ResponseStatus.DOWNLOADING -> {
				progressBarDownloading.visibility = View.VISIBLE
			}
			ResponseStatus.FAIL -> {
				progressBarDownloading.visibility = View.INVISIBLE
			}
		}

	}
}