package com.kaerusworld.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaerusworld.newsapp.presentation.navigation.NewsAppNavigation
import com.kaerusworld.newsapp.presentation.viewmodel.NewsViewModel
import com.kaerusworld.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val viewModel: NewsViewModel = hiltViewModel()
                NewsAppNavigation(newsViewModel = viewModel)
            }
        }
    }
}