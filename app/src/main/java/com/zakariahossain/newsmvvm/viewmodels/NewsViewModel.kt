package com.zakariahossain.newsmvvm.viewmodels

import android.util.Log
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.zakariahossain.newsmvvm.util.Resource.*
import com.zakariahossain.newsmvvm.util.Resource
import com.zakariahossain.newsmvvm.models.Article
import com.zakariahossain.newsmvvm.models.NewsResponse
import com.zakariahossain.newsmvvm.util.checkError
import com.zakariahossain.newsmvvm.repositories.NewsRepository

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var breakingNesPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null
    val breakingNewsLiveData = MutableLiveData<Resource<NewsResponse>>()

    var searchNewsPageNumber = 1
    val searchNewsResponse: NewsResponse? = null
    val searchNewsLiveData = MutableLiveData<Resource<NewsResponse>>()

    init {
        breakingNews("us")
    }

    fun breakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Loading())
        try {
            val response = newsRepository.getBreakingNews(countryCode, breakingNesPageNumber)
            breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
        } catch (e: Exception) {
            checkError(e, breakingNewsLiveData)
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Loading())

        try {
            val response = newsRepository.searchNews(searchQuery, searchNewsPageNumber)
            searchNewsLiveData.postValue(handleSearchNewsResponse(response))
        } catch (e: Exception) {
            checkError(e, searchNewsLiveData)
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNesPageNumber++

                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val newArticle = resultResponse.articles
                    breakingNewsResponse?.articles?.addAll(newArticle)
                }

                return Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Success(it)
            }
        }

        return Error(response.message())
    }

    fun savedArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews(): LiveData<List<Article>> = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
