package com.ahmedfathy.articles.repository


import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.network.ArticlesClient
import com.skydoves.sandwich.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val articlesClient: ArticlesClient,
    private val articleDao: ArticleDao
) {

    @WorkerThread
    suspend fun fetchArticlesList(
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = articlesClient.fetchArticlesList()
        response.suspendOnSuccess {
            var articles = data!!.results
            articles!!.forEach() { article ->
                var newArticle = ArticleEntity(
                    id = article.id,
                    title = article.title,
                    source = article.source,
                    section = article.section,
                    publishedDate = article.publishedDate
                )
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


