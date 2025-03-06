package com.kaerusworld.newsapp.presentation.navigation

sealed class NavigationRoutes(val route: String) {
    object NewsList : NavigationRoutes("newsList")
    object NewsDetails : NavigationRoutes("newsDetails/{articleId}") {
        fun createRoute(articleId: Int) = "newsDetails/$articleId"
    }
    object LikeComment : NavigationRoutes("likeComment/{articleId}") {
        fun createRoute(articleId: Int) = "likeComment/$articleId"
    }
}