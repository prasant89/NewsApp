package com.kaerusworld.newsapp.data.repository

import android.util.Log
import com.kaerusworld.newsapp.data.db.NewsDao
import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.data.network.NewsApiService
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import com.kaerusworld.newsapp.common.NetworkUtils
import com.kaerusworld.newsapp.data.network.ArticleInfoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao,
    private val networkUtils: NetworkUtils,
    private val apiArticleInfo: ArticleInfoApi
) : NewsRepository {

    override suspend fun fetchNews(apiKey: String): Flow<List<NewsArticle>> = flow {
        if (networkUtils.isNetworkAvailable()) {
            try {
                val response = apiService.getTopHeadlines(apiKey)
                if (response.isSuccessful) {
                    response.body()?.articles?.let {
                        newsDao.insertArticles(it)
                    }
                } else {
                    throw Exception("Network error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NewsRepository", "Error fetching news", e)
            }
        } else {
            Log.e("NewsRepository", "No network connection")
        }
        emitAll(newsDao.getArticles())
    }

    override fun getOfflineNews(): Flow<List<NewsArticle>> = newsDao.getArticles()

    override suspend fun getArticleById(articleId: Int): NewsArticle? {
        return withContext(Dispatchers.IO) {
            newsDao.getArticleById(articleId)
        }
    }

    override suspend fun getLikes(articleUrl: String): Int {
        return try {
            apiArticleInfo.getLikes(articleUrl.toArticleId()).likes
        }catch (e: Exception) {
            Log.e("NewsRepository", "Failed to fetch comments: ${e.message}")
            150 // Return mock data
        }
    }

    override suspend fun getComments(articleUrl: String): Int {
        return try {
            apiArticleInfo.getComments(articleUrl.toArticleId()).comments
        }catch (e: Exception) {
            Log.e("NewsRepository", "Failed to fetch comments: ${e.message}")
            50 // Return mock data
        }
    }

    private fun String.toArticleId(): String {
        return this.removePrefix("https://").replace("/", "-")
    }
}

