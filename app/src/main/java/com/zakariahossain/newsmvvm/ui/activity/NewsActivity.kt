package com.zakariahossain.newsmvvm.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zakariahossain.newsmvvm.R
import com.zakariahossain.newsmvvm.data.db.ArticleDatabase
import com.zakariahossain.newsmvvm.repositories.NewsRepository
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModel
import com.zakariahossain.newsmvvm.viewmodels.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        bottomNavView.setupWithNavController(navHostFragment.findNavController())

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
    }
}
