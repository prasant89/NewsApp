package com.kaerusworld.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaerusworld.newsapp.data.model.NewsArticle
import com.kaerusworld.newsapp.domain.repository.NewsRepository
import com.kaerusworld.newsapp.domain.usecase.FetchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val fetchNewsUseCase: FetchNewsUseCase,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles

    private val _errorState = MutableStateFlow<String?>(null)  // Error state for managing errors
    val errorState: StateFlow<String?> = _errorState

    // Fetches news using the use case, and handles errors
    fun fetchNews(apiKey: String) {
        viewModelScope.launch {
            try {
                // Fetch news articles using the provided API key
                val articles = fetchNewsUseCase(apiKey)
                _newsArticles.value = articles
                _errorState.value = null // Reset error on successful fetch
            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.value = "Failed to fetch news: ${e.message}" // Set error message
            }
        }
    }

    // Loads offline news from the repository and updates state
    fun loadOfflineNews() {
        viewModelScope.launch {
            newsRepository.getOfflineNews()
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList()) // Simplify Flow to StateFlow conversion
                .collect { articles ->
                    _newsArticles.value = articles
                }
        }
    }
}