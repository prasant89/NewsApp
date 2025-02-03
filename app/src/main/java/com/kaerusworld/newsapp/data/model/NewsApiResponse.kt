package com.kaerusworld.newsapp.data.model

import com.google.gson.annotations.SerializedName

data class NewsApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<NewsArticle>
)