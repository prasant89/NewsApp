package com.kaerusworld.newsapp.data.network

import com.kaerusworld.newsapp.data.model.CommentsResponse
import com.kaerusworld.newsapp.data.model.LikesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleInfoApi {
    @GET("likes/{articleId}")
    suspend fun getLikes(@Path("articleId") articleId: String): LikesResponse

    @GET("comments/{articleId}")
    suspend fun getComments(@Path("articleId") articleId: String): CommentsResponse
}