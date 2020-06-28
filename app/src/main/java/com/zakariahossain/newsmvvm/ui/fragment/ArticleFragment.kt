package com.zakariahossain.newsmvvm.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zakariahossain.newsmvvm.R
import com.zakariahossain.newsmvvm.ui.activity.NewsActivity
import com.zakariahossain.newsmvvm.util.snackBar
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        val article = args.article

        webView.apply {
            overScrollMode = View.OVER_SCROLL_NEVER
            loadUrl(article.url)
        }

        fab.setOnClickListener {
            viewModel.savedArticle(article)
            view.snackBar("Article saved successfully")
        }
    }
}
