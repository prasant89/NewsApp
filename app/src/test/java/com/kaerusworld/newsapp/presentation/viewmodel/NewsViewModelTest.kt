package com.kaerusworld.newsapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kaerusworld.newsapp.data.model.NewsArticle
import com.kaerusworld.newsapp.data.model.Source
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import com.kaerusworld.newsapp.domain.usecase.FetchNewsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
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

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {
    // Mocks
    private lateinit var fetchNewsUseCase: FetchNewsUseCase
    private lateinit var newsRepository: NewsRepository
    private lateinit var viewModel: NewsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher for testing
        fetchNewsUseCase = mockk()
        newsRepository = mockk()
        viewModel = NewsViewModel(fetchNewsUseCase, newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher after tests
    }

    @Test
    fun `fetchNews should update newsArticles on success`() = runTest {

        coEvery { fetchNewsUseCase("test_api_key") } returns getMockNewsArticles()

        // When
        viewModel.fetchNews("test_api_key")
        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutines

        // Then
        assertEquals(getMockNewsArticles(), viewModel.newsArticles.value)
        assertNull(viewModel.errorState.value) // No errors should be present
    }

    @Test
    fun `fetchNews should set errorState on failure`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { fetchNewsUseCase("test_api_key") } throws exception

        // When
        viewModel.fetchNews("test_api_key")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals("Failed to fetch news: Network error", viewModel.errorState.value)
        // assertEquals(emptyList(), viewModel.newsArticles.value) // News list should remain empty
    }

    @Test
    fun `loadOfflineNews should update newsArticles from repository`() = runTest {
        // Given

        every { newsRepository.getOfflineNews() } returns flowOf(getMockNewsArticles())

        // When
        viewModel.loadOfflineNews()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(getMockNewsArticles(), viewModel.newsArticles.value)
    }
}

fun getMockNewsArticles(): List<NewsArticle> {
    return listOf(
        NewsArticle(
            id = 1,
            source = Source(id = "cnn", name = "CNN"),
            author = "John Doe",
            title = "Breaking News: Major Event Happening Now",
            description = "A major event has occurred, shaking up the world.",
            url = "https://www.cnn.com/breaking-news",
            imageUrl = "https://www.cnn.com/images/breaking-news.jpg",
            publishedAt = "2025-02-02T02:05:00Z",
            content = "Full details about the major event..."
        )
    )
}