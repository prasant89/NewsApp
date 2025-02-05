package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.data.model.Source
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetArticleByIdUseCaseTest {

    private lateinit var getArticleByIdUseCase: GetArticleByIdUseCase

    @MockK
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getArticleByIdUseCase = GetArticleByIdUseCase(newsRepository)
    }

    @Test
    fun `invoke should return article when found`() = runTest {
        val mockArticle = NewsArticle(
            id = 1,
            source = Source(id = "cnn", name = "CNN"),
            author = "John Doe",
            title = "Breaking News",
            description = "Major event happening",
            url = "https://www.example.com",
            imageUrl = "https://www.example.com/image.jpg",
            publishedAt = "2025-02-01T00:00:00Z",
            content = "Full content of the news article"
        )
        coEvery { newsRepository.getArticleById(1) } returns mockArticle

        val result = getArticleByIdUseCase(1)

        assertEquals(mockArticle, result)
        coVerify { newsRepository.getArticleById(1) }
    }

    @Test
    fun `invoke should return null when article not found`() = runTest {
        coEvery { newsRepository.getArticleById(999) } returns null

        val result = getArticleByIdUseCase(999)

        assertNull(result)
        coVerify { newsRepository.getArticleById(999) }
    }
}