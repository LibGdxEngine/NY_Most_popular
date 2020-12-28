package com.ahmedfathy.articles.network


import com.ahmedfathy.articles.models.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ArticlesService {
    @GET
    suspend fun fetchArticlesList(
        @Url url :String
    ): ApiResponse<ArticleResponse>
}