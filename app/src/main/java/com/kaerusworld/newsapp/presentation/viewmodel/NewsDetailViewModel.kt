package com.kaerusworld.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaerusworld.newsapp.domain.usecase.GetNewsCommentsUseCase
import com.kaerusworld.newsapp.domain.usecase.GetNewsLikesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val getNewsLikesUseCase: GetNewsLikesUseCase,
    private val getNewsCommentsUseCase: GetNewsCommentsUseCase
) : ViewModel() {

    private val _likes = MutableStateFlow(0)
    val likes = _likes.asStateFlow()

    private val _comments = MutableStateFlow(0)
    val comments = _comments.asStateFlow()

    fun fetchArticleInfo(url: String) {
        viewModelScope.launch {
            _likes.value = getNewsLikesUseCase(url)
            _comments.value = getNewsCommentsUseCase(url)
        }
    }
}
