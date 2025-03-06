package com.kaerusworld.newsapp.data.repository

import com.kaerusworld.newsapp.data.db.NewsDao
import com.kaerusworld.newsapp.data.model.NewsApiResponse
import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.data.model.Source
import com.kaerusworld.newsapp.data.network.NewsApiService
import com.kaerusworld.newsapp.common.NetworkUtils
import com.kaerusworld.newsapp.data.network.ArticleInfoApi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {

    private lateinit var newsRepository: NewsRepositoryImpl

    @MockK
    private lateinit var mockApiService: NewsApiService

    @MockK
    private lateinit var mockArticleInfoApi: ArticleInfoApi

    @MockK(relaxed = true)
    private lateinit var mockNewsDao: NewsDao

    @MockK(relaxed = true)
    private lateinit var mockNetworkUtils: NetworkUtils

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        newsRepository = NewsRepositoryImpl(mockApiService, mockNewsDao, mockNetworkUtils,mockArticleInfoApi)
    }

    @Test
    fun `fetchNews should emit articles from dao when network is available and api call is successful`() = runBlocking {
        val apiKey = "testApiKey"
        val country = "us"
        val articles = getMockNewsArticles()
        val response = Response.success(NewsApiResponse(status = "ok", totalResults = articles.size, articles = articles))

        every { mockNetworkUtils.isNetworkAvailable() } returns true
        coEvery { mockApiService.getTopHeadlines(apiKey, country) } returns response
        coEvery { mockNewsDao.insertArticles(articles) } just Runs
        coEvery { mockNewsDao.getArticles() } returns flowOf(articles)

        val result = newsRepository.fetchNews(apiKey).first()

        assertEquals(articles, result)
        coVerify(exactly = 1) { mockNewsDao.insertArticles(articles) }
        coVerify(exactly = 1) { mockNewsDao.getArticles() }
        coVerify(exactly = 1) { mockApiService.getTopHeadlines(apiKey, country) }
    }

    @Test
    fun `getOfflineNews should emit articles from dao`() = runBlocking {
        val fakeArticles = getMockNewsArticles()
        coEvery { mockNewsDao.getArticles() } returns flowOf(fakeArticles)

        val result = newsRepository.getOfflineNews().first()

        assertEquals(fakeArticles, result)
        coVerify(exactly = 1) { mockNewsDao.getArticles() }
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