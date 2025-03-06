package com.kaerusworld.newsapp.domain.repository

import com.kaerusworld.newsapp.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun fetchNews(apiKey: String): Flow<List<NewsArticle>>
    fun getOfflineNews(): Flow<List<NewsArticle>>
    suspend  fun getArticleById(articleId: Int): NewsArticle?
    suspend fun getLikes(articleUrl: String): Int
    suspend fun getComments(articleUrl: String): Int
}