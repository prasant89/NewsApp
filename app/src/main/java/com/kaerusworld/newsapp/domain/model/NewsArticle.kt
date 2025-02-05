package com.kaerusworld.newsapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.kaerusworld.newsapp.data.model.Converters
import com.kaerusworld.newsapp.data.model.Source
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "news_articles")
@TypeConverters(Converters::class)
data class NewsArticle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("source") val source: Source,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?
) {
    fun getPublishedDate(): Date? {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            format.parse(publishedAt ?: "")
        } catch (e: Exception) {
            null
        }
    }
}