package com.zakariahossain.newsmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zakariahossain.newsmvvm.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long //insert + update

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}