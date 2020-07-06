package com.zakariahossain.newsmvvm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zakariahossain.newsmvvm.R
import com.zakariahossain.newsmvvm.ui.activity.NewsActivity
import com.zakariahossain.newsmvvm.ui.adapters.NewsAdapter
import com.zakariahossain.newsmvvm.util.*
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val TAG = "BreakingNewsFragment"
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }

            findNavController().navigate(
                R.id.action_breakingNews_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Loading -> {
                    paginationProgressBar.isShowProgressBar(true)
                    isLoading = true
                }

                is Resource.Success -> {
                    paginationProgressBar.isShowProgressBar(false)
                    isLoading = false

                    response.data?.let { newsResponse ->
                        newsAdapter.differList.submitList(newsResponse.articles.toList())
                        val size = newsAdapter.differList.currentList.size
                        noBreakingNews.isVisibleView(size)

                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNesPageNumber == totalPages
                        if (isLastPage) rvBreakingNews.setPadding(0, 0, 0, 0)

                        Log.d(TAG, "totalPages: $totalPages, breakingNesPageNumber: ${viewModel.breakingNesPageNumber}" +
                                " size: ${newsResponse.articles.size} ")
                    }
                }

                is Resource.Error -> {
                    paginationProgressBar.isShowProgressBar(false)
                    isLoading = false

                    response.message?.let { message ->
                        val error = "An error occurred: $message"
                        view.snackBar(error, Snackbar.LENGTH_LONG)
                        Log.d(TAG, error)
                    }
                }
            }
        })
    }

    private val rvScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.breakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(rvScrollListener)
        }
    }
}
