package com.zakariahossain.newsmvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakariahossain.newsmvvm.models.Article
import com.zakariahossain.newsmvvm.models.NewsResponse
import com.zakariahossain.newsmvvm.repositories.NewsRepository
import com.zakariahossain.newsmvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

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
        breakingNewsLiveData.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNesPageNumber)
        breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPageNumber)
        searchNewsLiveData.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNesPageNumber++

                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    //val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    //oldArticle?.addAll(newArticle)

                    breakingNewsResponse?.articles?.addAll(newArticle)
                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    fun savedArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews(): LiveData<List<Article>> = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
