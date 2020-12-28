package com.ahmedfathy.articles.repository


import android.util.Log
import androidx.annotation.WorkerThread
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.mapper.mapper.fromModelToEntity
import com.ahmedfathy.articles.network.ArticlesClient
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val articlesClient: ArticlesClient,
    private val articleDao: ArticleDao
) {
    //this repo uses a caching mechanism to store data after first time using internet
    //we fetch data from api and store it in room database
    //if no network connection -> get data from room
    @WorkerThread
    suspend fun fetchArticlesList(
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = articlesClient.fetchArticlesList()
        response.suspendOnSuccess {
            var articles = data!!.results
            articles?.forEach() { article ->
                var newArticle = fromModelToEntity(article)
                articleDao.insert(newArticle)
                emit(articleDao.getArticles("", SortOrder.BY_DATE, false))
                onSuccess()
            }
        }
            // handles the case when the API request gets an error response.
            // e.g., internal server error.
            .suspendOnError {
            Log.e("Main", "[Code: ${this.raw.code}]: ${this.raw.message}")
            onError("[Code: ${this.raw.code}]: ${this.raw.message}")

        }
            // handles the case when the API request gets an exception response.
            // e.g., network connection error.
            .suspendOnException {
            emit(articleDao.getArticles("" , SortOrder.BY_DATE , false))
            Log.e("Main", "$message")
            onError(message)
        }
    }.flowOn(Dispatchers.IO)

}


