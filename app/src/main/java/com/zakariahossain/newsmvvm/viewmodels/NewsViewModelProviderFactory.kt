package com.zakariahossain.newsmvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zakariahossain.newsmvvm.repositories.NewsRepository

class NewsViewModelProviderFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}