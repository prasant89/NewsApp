package com.kaerusworld.newsapp.data.network

import com.kaerusworld.newsapp.data.model.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String = "us"
    ): Response<NewsApiResponse>
}