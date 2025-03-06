package com.kaerusworld.newsapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kaerusworld.newsapp.presentation.viewmodel.NewsDetailViewModel

@Composable
fun LikeCommentScreen(
    articleId: Int,
    viewModel: NewsDetailViewModel,
    navController: NavController
) {
    val likesInfo = viewModel.likes.collectAsState().value
    val commentsInfo = viewModel.comments.collectAsState().value

    LaunchedEffect(articleId) {
        viewModel.fetchArticleInfo(articleId.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Likes: ${likesInfo}", fontSize = 18.sp)
        Text(text = "Comments: ${commentsInfo}", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Details")
        }
    }
}