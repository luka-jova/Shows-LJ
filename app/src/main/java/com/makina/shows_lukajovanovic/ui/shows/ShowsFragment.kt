package com.makina.shows_lukajovanovic.ui.shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.makina.shows_lukajovanovic.R
import com.makina.shows_lukajovanovic.data.model.ShowsListResponse
import com.makina.shows_lukajovanovic.data.network.ResponseStatus
import com.makina.shows_lukajovanovic.ui.MainContainerActivity
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment
import com.makina.shows_lukajovanovic.ui.episodes.EpisodesFragment.Companion.EPISODES_FRAGMENT_TAG
import kotlinx.android.synthetic.main.fragment_shows.*



class ShowsFragment : Fragment() {
	companion object {
		const val FRAGMENT_NAME = "Shows_FRAGMENT"
		const val LAYOUT_LIST = 0
		const val LAYOUT_GRID = 1
	}

	//TODO handle error messages for this and other Fragments
	private lateinit var adapter: ShowsRecyclerAdapter
	private lateinit var viewModel: ShowsViewModel
	private var curLayout = LAYOUT_GRID

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_shows, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
		viewModel.showsListResponseLiveData.observe(this, Observer {
			updateUI()
		})
		generateRecyclerView()
		progressBarDownloading.visibility = View.INVISIBLE
		viewModel.getShowsList()
		fabToggleLayout.setOnClickListener {
			curLayout =
				if(curLayout == LAYOUT_LIST) LAYOUT_GRID
				else LAYOUT_LIST
			generateRecyclerView()
		}
	}

	private fun generateRecyclerView() {
		val scrollPosition =
			when {
				recyclerViewShows.layoutManager is LinearLayoutManager -> (recyclerViewShows.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
				recyclerViewShows.layoutManager is GridLayoutManager -> (recyclerViewShows.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
				else -> 0
			}
		val viewId = if (curLayout == LAYOUT_LIST) R.layout.layout_show else R.layout.layout_show_card
		adapter = ShowsRecyclerAdapter(viewId) { showId, title ->
			if (fragmentManager?.findFragmentByTag(EPISODES_FRAGMENT_TAG) != null) fragmentManager?.popBackStack()
			fragmentManager?.beginTransaction()?.apply {
				replace(
					(activity as MainContainerActivity).slaveContainerId,
					EpisodesFragment.newInstance(showId, title),
					EpisodesFragment.EPISODES_FRAGMENT_TAG
				)
				addToBackStack("Episodes $showId")
				commit()
			}
		}
		recyclerViewShows.adapter = adapter
		//recyclerViewShows.addItemDecoration(MarginDecoration(this))
		recyclerViewShows.layoutManager =
			if (curLayout == LAYOUT_LIST) LinearLayoutManager(requireContext())
			else GridLayoutManager(requireContext(), 2)
		updateUI()
		recyclerViewShows.scrollToPosition(scrollPosition)
	}

	private fun updateUI() {
		val response: ShowsListResponse? = viewModel.showsListResponseLiveData.value
		when (response?.status) {
			ResponseStatus.SUCCESS -> {
				adapter.setData(response.showsList ?: listOf())
				progressBarDownloading.visibility = View.INVISIBLE
			}
			ResponseStatus.DOWNLOADING -> {
				progressBarDownloading.visibility = View.VISIBLE
			}
			ResponseStatus.FAIL -> {
				progressBarDownloading.visibility = View.INVISIBLE
				Toast.makeText(requireContext(), "Downloading failed", Toast.LENGTH_SHORT).show()
			}
		}
	}
	/*
	class MarginDecoration(context: Context) : RecyclerView.ItemDecoration() {
		private val margin: Int

		init {
			margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin)
		}

		fun getItemOffsets(
			outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
		) {
			outRect.set(margin, margin, margin, margin)
		}
	}*/
}

