/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("SpellCheckingInspection")

package com.ahmedfathy.articles.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.ahmedfathy.articles.MainCoroutinesRule
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.mapper.mapper.fromModelToEntity
import com.ahmedfathy.articles.models.ArticleResponse
import com.ahmedfathy.articles.network.ArticlesClient
import com.ahmedfathy.articles.network.ArticlesService
import com.ahmedfathy.articles.util.Constants
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.pokedex.utils.MockUtil.mockArticleList
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class MainRepositoryTest {

  private lateinit var repository: MainRepository
  private lateinit var client: ArticlesClient
  private val service: ArticlesService = mock()
  private val articleDao: ArticleDao = mock()

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @Before
  fun setup() {
    client = ArticlesClient(service)
    repository = MainRepository(client, articleDao)
  }

  @ExperimentalTime
  @Test
  fun fetchArticleListFromNetworkTest() = runBlocking {
    val mockData = ArticleResponse("OK" , "Copyright (c) 2020 The New York Times Company.  All Rights Reserved." , 20, results = mockArticleList())
    whenever(articleDao.getArticles("" , SortOrder.BY_DATE , false)).thenReturn(null)
    whenever(service.fetchArticlesList(Constants.BASE_URL)).thenReturn(ApiResponse.of { Response.success(mockData) })

    repository.fetchArticlesList(
      onSuccess = {},
      onError = {}
    ).test {
      var articleList = emptyList<ArticleEntity>()
      expectItem().collect {
        articleList = it
      }
      assertEquals(articleList[0].id, 100000007510162)
      assertEquals(articleList[0].title, "The ‘Red Slime’ Lawsuit That Could Sink Right-Wing Media")
      assertEquals(articleList[0].id, 100000007510162)
      expectComplete()
    }

    verify(articleDao, atLeastOnce()).getArticles("" , SortOrder.BY_DATE , false)
    verify(service, atLeastOnce()).fetchArticlesList(Constants.BASE_URL)
    verify(articleDao, atLeastOnce()).insert(fromModelToEntity(mockData.results.get(0)))
  }

  @ExperimentalTime
  @Test
  fun fetchArticleListFromDatabaseTest() = runBlocking {
    whenever(service.fetchArticlesList(Constants.BASE_URL)).thenReturn(ApiResponse.of { Response.error(404 , null) })

    repository.fetchArticlesList(
      onSuccess = {},
      onError = {}
    ).test {
      var articleList = emptyList<ArticleEntity>()
      expectItem().collect {
        articleList = it
      }
      assertEquals(articleList.get(0).id, 100000007510162)
      expectComplete()
    }

    verify(articleDao, atLeastOnce()).getArticles("" , SortOrder.BY_DATE , false)
    verifyNoMoreInteractions(service)
  }
}
