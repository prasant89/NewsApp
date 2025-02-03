package com.kaerusworld.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaerusworld.newsapp.data.model.NewsArticle
import com.kaerusworld.newsapp.presentation.screens.NewsDetailsScreen
import com.kaerusworld.newsapp.presentation.screens.NewsListScreen
import com.kaerusworld.newsapp.presentation.viewmodel.NewsViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.google.gson.Gson

@Composable
fun NewsAppNavigation(newsViewModel: NewsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "newsList") {
        composable("newsList") {
            NewsListScreen(viewModel = newsViewModel) { selectedArticle ->
                val encodedJson = encodeArticleToJson(selectedArticle)
                navController.navigate("newsDetails/$encodedJson")
            }
        }
        composable(
            route = "newsDetails/{articleJson}",
            arguments = listOf(navArgument("articleJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("articleJson") ?: ""
            val article = decodeJsonToArticle(json)
            NewsDetailsScreen(newsArticle = article,navController, onBackClick = { navController.popBackStack() })
        }
    }
}

fun encodeArticleToJson(article: NewsArticle): String {
    val json = Gson().toJson(article)
    return URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
}

fun decodeJsonToArticle(json: String): NewsArticle? {
    return runCatching {
        val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
        Gson().fromJson(decodedJson, NewsArticle::class.java)
    }.getOrNull()
}