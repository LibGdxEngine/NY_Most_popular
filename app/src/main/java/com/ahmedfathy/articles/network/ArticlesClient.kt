package com.ahmedfathy.articles.network

import com.ahmedfathy.articles.models.ArticleResponse
import com.ahmedfathy.articles.util.Constants
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ArticlesClient @Inject constructor(private val articlesService: ArticlesService) {
    suspend fun fetchArticlesList(): ApiResponse<ArticleResponse> = articlesService.fetchArticlesList(Constants.BASE_URL)
}