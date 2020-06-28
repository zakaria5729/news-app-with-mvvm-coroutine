package com.zakariahossain.newsmvvm.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zakariahossain.newsmvvm.ui.activity.NewsActivity

import com.zakariahossain.newsmvvm.R
import com.zakariahossain.newsmvvm.ui.adapters.NewsAdapter
import com.zakariahossain.newsmvvm.util.isShowProgressBar
import com.zakariahossain.newsmvvm.util.isVisibleView
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

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
                R.id.action_savedNews_to_articleFragment,
                bundle
            )
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differList.submitList(articles)
            val size = newsAdapter.differList.currentList.size
            noSavedNews.isVisibleView(size)
        })

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder:
            RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differList.currentList[position]
                viewModel.deleteArticle(article)

                val size = newsAdapter.differList.currentList.size
                Log.d("fdklf", size.toString())
                noSavedNews.isVisibleView(size)

                Snackbar.make(view, "Article deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.savedArticle(article)
                    }.show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
