package com.kaerusworld.newsapp.data.model

import com.google.gson.annotations.SerializedName
import com.kaerusworld.newsapp.domain.model.NewsArticle

data class NewsApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<NewsArticle>
)