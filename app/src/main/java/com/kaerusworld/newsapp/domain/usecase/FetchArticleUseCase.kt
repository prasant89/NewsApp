package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchArticleUseCase @Inject constructor(private val repository: NewsRepository) {
    /**
     * Fetch news articles.
     * This function abstracts the logic of fetching news data and handles success or failure scenarios.
     */
    suspend operator fun invoke(apiKey: String): List<NewsArticle> = repository.fetchNews(apiKey).first()
}