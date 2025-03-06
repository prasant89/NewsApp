package com.kaerusworld.newsapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kaerusworld.newsapp.presentation.screens.header.NewsTopBar
import com.kaerusworld.newsapp.presentation.viewmodel.NewsViewModel
import com.kaerusworld.newsapp.presentation.ui.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreen(
    articleId: Int,
    viewModel: NewsViewModel,
    navController: NavController,
    onLikeCommentClick: () -> Unit
) {
    LaunchedEffect(articleId) {
        viewModel.loadArticleById(articleId)
    }

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            NewsTopBar(
                title = "${articleId?: "N/A"} / 6",
                showBackButton = true,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> NewsDetailsContent(uiState.data, onLikeCommentClick = onLikeCommentClick)
                is UiState.Error -> Text(uiState.message, color = MaterialTheme.colorScheme.error)
                UiState.Idle -> Unit
            }
        }
    }
}