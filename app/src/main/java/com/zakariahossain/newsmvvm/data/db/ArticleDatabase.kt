package com.zakariahossain.newsmvvm.data.db

import androidx.room.Room
import androidx.room.Database
import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zakariahossain.newsmvvm.models.Article

@TypeConverters(Converters::class)
@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}