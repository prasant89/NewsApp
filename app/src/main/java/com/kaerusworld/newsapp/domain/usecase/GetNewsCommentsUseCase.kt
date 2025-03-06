package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsCommentsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(articleUrl: String): Int {
        return repository.getComments(articleUrl)
    }
}
