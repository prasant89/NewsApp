package com.kaerusworld.newsapp.presentation.viewmodel

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.data.model.Source
import com.kaerusworld.newsapp.domain.usecase.FetchArticleUseCase
import com.kaerusworld.newsapp.domain.usecase.GetArticleByIdUseCase
import com.kaerusworld.newsapp.domain.usecase.GetOfflineNewsUseCase
import com.kaerusworld.newsapp.presentation.ui.state.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @MockK private lateinit var fetchArticleUseCase: FetchArticleUseCase
    @MockK private lateinit var getOfflineNewsUseCase: GetOfflineNewsUseCase
    @MockK private lateinit var getArticleByIdUseCase: GetArticleByIdUseCase
    private lateinit var viewModel: NewsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        // Initialize the viewModel
        viewModel = NewsViewModel(
            fetchArticleUseCase,
            getOfflineNewsUseCase,
            getArticleByIdUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNews should update newsState on success`() = runTest {
        coEvery { fetchArticleUseCase("test_api_key") } returns getMockNewsArticles()

        viewModel.fetchNews("test_api_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.newsState.first()
        assert(state is UiState.Success)
        assertEquals(getMockNewsArticles(), (state as UiState.Success).data)    }

    @Test
    fun `fetchNews should set error state on failure`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { fetchArticleUseCase("test_api_key") } throws exception

        viewModel.fetchNews("test_api_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.newsState.first()
        assert(state is UiState.Error)
        assertEquals("Failed to fetch news: Network error", (state as UiState.Error).message)
    }

    @Test
    fun `loadOfflineNews should update newsState with offline news`() = runTest {
        coEvery { getOfflineNewsUseCase() } returns flowOf(getMockNewsArticles())

        viewModel.loadOfflineNews()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.newsState.first()
        assert(state is UiState.Success)
        assertEquals(getMockNewsArticles(), (state as UiState.Success).data)
    }

    @Test
    fun `loadArticleById should update uiState with the correct article`() = runTest {
        val article = getMockNewsArticles().first()
        coEvery { getArticleByIdUseCase(article.id) } returns article

        viewModel.loadArticleById(article.id)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assert(state is UiState.Success)
        assertEquals(article, (state as UiState.Success).data)
    }

    @Test
    fun `loadArticleById should return error if article not found`() = runTest {
        coEvery { getArticleByIdUseCase(999) } returns null

        viewModel.loadArticleById(999)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assert(state is UiState.Error)
        assertEquals("Article not found", (state as UiState.Error).message)
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