package com.kaerusworld.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaerusworld.newsapp.presentation.screens.ArticleDetailScreen
import com.kaerusworld.newsapp.presentation.screens.LikeCommentScreen
import com.kaerusworld.newsapp.presentation.screens.NewsDetailsScreen
import com.kaerusworld.newsapp.presentation.screens.NewsListScreen

@Composable
fun NewsAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationRoutes.NewsList.route) {
        composable(NavigationRoutes.NewsList.route) {
            NewsListScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                onArticleClick = { article ->
                    navController.navigate(NavigationRoutes.NewsDetails.createRoute(article.id))
                }
            )
        }

        composable(
            route = NavigationRoutes.NewsDetails.route,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: return@composable
            NewsDetailsScreen(
                articleId = articleId,
                viewModel = hiltViewModel(),
                navController = navController,
                onLikeCommentClick = {
                    navController.navigate(NavigationRoutes.LikeComment.createRoute(articleId))
                }
            )
        }

        // Article Detail Screen (if navigating by URL)
        composable("articleDetail/{articleUrl}") { backStackEntry ->
            val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            ArticleDetailScreen(articleUrl, navController = navController)
        }

        // Like & Comment Screen
        composable(
            route = NavigationRoutes.LikeComment.route,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: return@composable
            LikeCommentScreen(
                articleId = articleId,
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}
