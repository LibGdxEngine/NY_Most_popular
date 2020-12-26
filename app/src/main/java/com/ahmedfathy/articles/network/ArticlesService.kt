package com.ahmedfathy.articles.network


import com.ahmedfathy.articles.models.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ArticlesService {
    //https://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/7.json?api-key=kZv1Cn0MmAw7zaYTVF0Tm7apPed9exy0

    @GET
    suspend fun fetchArticlesList(
        @Url url :String
    ): ApiResponse<ArticleResponse>
}