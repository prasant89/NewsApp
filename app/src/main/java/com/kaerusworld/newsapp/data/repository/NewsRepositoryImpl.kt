package com.kaerusworld.newsapp.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.kaerusworld.newsapp.data.db.NewsDao
import com.kaerusworld.newsapp.data.model.NewsArticle
import com.kaerusworld.newsapp.data.network.NewsApiService
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao,
    private val connectivityManager: ConnectivityManager
) : NewsRepository {
    // Function to check network connection
    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override suspend fun fetchNews(apiKey: String): Flow<List<NewsArticle>> = flow {
        if (isNetworkAvailable()) {
            // Log request details
            val requestUrl = "https://newsapi.org/v2/top-headlines?apiKey=$apiKey&country=us"
            Log.d("NewsRepository", "Making request to: $requestUrl")

            // Log the request headers (optional)
            Log.d("NewsRepository", "Request headers: ${apiService.getTopHeadlines(apiKey)}")

            // Network available: fetch news from API and store in DB
            try {
                val response = apiService.getTopHeadlines(apiKey)
                // Log the response details
                Log.d("NewsRepository", "Response code: ${response.code()}")
                Log.d("NewsRepository", "Response message: ${response.message()}")

                // Log the response body (in case of success or error)
                response.body()?.let {
                    Log.d("NewsRepository", "Response body: ${it.articles}")
                }

                if (response.isSuccessful) {
                    response.body()?.articles?.let {
                        newsDao.insertArticles(it)  // Store articles in DB
                    }
                } else {
                    // Log error body if response is not successful
                    response.errorBody()?.string()?.let {
                        Log.e("NewsRepository", "Error Body: $it")
                    }
                    throw Exception("Network error: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e("NewsRepository", "Network error", e)
            } catch (e: HttpException) {
                Log.e("NewsRepository", "HTTP error", e)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Unexpected error", e)
            }
        } else {
            Log.e("NewsRepository", "No network connection")
        }

        // Emit data from the local database (either after fetching from network or offline)
        emitAll(newsDao.getArticles())
    }

    override fun getOfflineNews(): Flow<List<NewsArticle>> = newsDao.getArticles()
}

