package com.kaerusworld.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaerusworld.newsapp.domain.model.NewsArticle
import com.kaerusworld.newsapp.domain.usecase.FetchArticleUseCase
import com.kaerusworld.newsapp.domain.usecase.GetArticleByIdUseCase
import com.kaerusworld.newsapp.domain.usecase.GetOfflineNewsUseCase
import com.kaerusworld.newsapp.common.Constants
import com.kaerusworld.newsapp.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val fetchArticleUseCase: FetchArticleUseCase,
    private val getOfflineNewsUseCase: GetOfflineNewsUseCase,
    private val getArticleByIdUseCase: GetArticleByIdUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<UiState<List<NewsArticle>>>(UiState.Loading)
    val newsState: StateFlow<UiState<List<NewsArticle>>> = _newsState

    private val _selectedArticle = MutableStateFlow<NewsArticle?>(null)
    val selectedArticle: StateFlow<NewsArticle?> = _selectedArticle.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<NewsArticle>>(UiState.Loading)
    val uiState: StateFlow<UiState<NewsArticle>> = _uiState.asStateFlow()


    init {
        fetchNews(Constants.API_KEY)
    }

    fun fetchNews(apiKey: String) {
        viewModelScope.launch {
            _newsState.value = UiState.Loading
            try {
                val articles = fetchArticleUseCase(apiKey)
                _newsState.value = UiState.Success(articles)
            } catch (e: Exception) {
                e.printStackTrace()
                _newsState.value = UiState.Error("Failed to fetch news: ${e.message}")
            }
        }
    }

    fun loadOfflineNews() {
        viewModelScope.launch {
            _newsState.value = UiState.Loading
            getOfflineNewsUseCase().catch { e ->
                _newsState.value = UiState.Error(e.message ?: "Unknown Error")
            }
                .collectLatest { articles ->
                    _newsState.value = UiState.Success(articles)
                }
        }
    }

    fun loadArticleById(articleId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val article = getArticleByIdUseCase(articleId)
                _uiState.value = article?.let { UiState.Success(it) }
                    ?: UiState.Error("Article not found")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load article: ${e.message}")
            }
        }
    }

}