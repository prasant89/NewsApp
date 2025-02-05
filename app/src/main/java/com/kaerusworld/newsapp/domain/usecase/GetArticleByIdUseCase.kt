package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetArticleByIdUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(articleId: Int): NewsArticle? {
        return newsRepository.getArticleById(articleId)
    }
}
