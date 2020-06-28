package com.zakariahossain.newsmvvm.repositories

import androidx.lifecycle.LiveData
import com.zakariahossain.newsmvvm.data.api.NewsApi
import com.zakariahossain.newsmvvm.data.api.RetrofitInstance
import com.zakariahossain.newsmvvm.data.db.ArticleDatabase
import com.zakariahossain.newsmvvm.models.Article

class NewsRepository(val db: ArticleDatabase) {
    private val newApi: NewsApi = RetrofitInstance.api

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newApi.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getSavedNews(): LiveData<List<Article>> = db.getArticleDao().getAllArticles()
}