package com.zakariahossain.newsmvvm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zakariahossain.newsmvvm.R
import com.zakariahossain.newsmvvm.ui.activity.NewsActivity
import com.zakariahossain.newsmvvm.ui.adapters.NewsAdapter
import com.zakariahossain.newsmvvm.util.*
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val TAG = "SearchNewsFragment"
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }

            findNavController().navigate(
                R.id.action_searchNews_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)

                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Loading -> paginationProgressBar.isShowProgressBar(true)

                is Resource.Success -> {
                    paginationProgressBar.isShowProgressBar(false)
                    response.data?.let { newsResponse ->
                        newsAdapter.differList.submitList(newsResponse.articles)
                        val size = newsAdapter.differList.currentList.size
                        noSearchNews.isVisibleView(size)
                    }
                }

                is Resource.Error -> {
                    paginationProgressBar.isShowProgressBar(false)
                    response.message?.let { message ->
                        val error = "An error occurred: $message"
                        view.snackBar(error, Snackbar.LENGTH_LONG)
                        Log.d(TAG, "An error occurred: $message")
                    }
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
