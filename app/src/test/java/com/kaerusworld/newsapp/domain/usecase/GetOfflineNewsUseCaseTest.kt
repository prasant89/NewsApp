package com.kaerusworld.newsapp.domain.usecase

import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetOfflineNewsUseCaseTest {

    private lateinit var getOfflineNewsUseCase: GetOfflineNewsUseCase

    @MockK
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getOfflineNewsUseCase = GetOfflineNewsUseCase(newsRepository)
    }

    @Test
    fun `invoke should return offline news articles`() = runTest {
        val mockNewsArticles = getMockNewsArticles()

        coEvery { newsRepository.getOfflineNews() } returns flowOf(mockNewsArticles)

        val result = getOfflineNewsUseCase()

        val resultList = result.first()
        assertEquals(mockNewsArticles, resultList)

        verify { newsRepository.getOfflineNews() }
    }

    @Test
    fun `invoke should return empty list when no offline news`() = runTest {
        coEvery { newsRepository.getOfflineNews() } returns flowOf(emptyList())

        val result = getOfflineNewsUseCase()

        val resultList = result.first()
        assertEquals(emptyList<NewsArticle>(), resultList)

        verify { newsRepository.getOfflineNews() }
    }
}

