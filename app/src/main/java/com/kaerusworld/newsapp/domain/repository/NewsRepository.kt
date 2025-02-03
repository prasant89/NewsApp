package com.kaerusworld.newsapp.domain.repository

import com.kaerusworld.newsapp.data.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun fetchNews(apiKey: String): Flow<List<NewsArticle>>
    fun getOfflineNews(): Flow<List<NewsArticle>>
}