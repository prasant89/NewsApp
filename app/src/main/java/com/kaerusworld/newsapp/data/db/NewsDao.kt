package com.kaerusworld.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaerusworld.newsapp.data.model.NewsArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Query("SELECT * FROM news_articles")
    fun getArticles(): Flow<List<NewsArticle>>
}