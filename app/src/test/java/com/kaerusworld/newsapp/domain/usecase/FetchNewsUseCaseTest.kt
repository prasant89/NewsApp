package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.data.model.Source
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@OptIn(ExperimentalCoroutinesApi::class)
class FetchNewsUseCaseTest {

    private lateinit var fetchArticleUseCase: FetchArticleUseCase
    private lateinit var newsRepository: NewsRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        newsRepository = mockk()
        fetchArticleUseCase = FetchArticleUseCase(newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return news articles on success`() = runTest {
        val fakeArticles = getMockNewsArticles()
        coEvery { newsRepository.fetchNews("test_api_key") } returns flowOf(fakeArticles)

        val result = fetchArticleUseCase("test_api_key")
        assertEquals(fakeArticles, result)
    }

    @Test
    fun `invoke should return empty list when repository returns no data`() = runTest {
        coEvery { newsRepository.fetchNews("test_api_key") } returns flowOf(emptyList())
        val result = fetchArticleUseCase("test_api_key")
        assertEquals(emptyList<NewsArticle>(), result)
    }
}

fun getMockNewsArticles(): List<NewsArticle> {
    return listOf(
        NewsArticle(
            id = 1,
            source = Source(id = "bbc", name = "BBC"),
            author = "Jane Doe",
            title = "Breaking News: Something Important Happened",
            description = "Details about an important event.",
            url = "https://www.bbc.com/news",
            imageUrl = "https://www.bbc.com/news/image.jpg",
            publishedAt = "2025-02-02T10:00:00Z",
            content = "Full content of the news article."
        )
    )
}