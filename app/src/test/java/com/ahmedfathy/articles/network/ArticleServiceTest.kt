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

package com.skydoves.pokedex.network

import com.ahmedfathy.articles.MainCoroutinesRule
import com.ahmedfathy.articles.network.ArticlesClient
import com.ahmedfathy.articles.network.ArticlesService
import com.ahmedfathy.articles.util.Constants
import com.nhaarman.mockitokotlin2.mock
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class ArticleServiceTest : ApiAbstract<ArticlesService>() {

  private lateinit var service: ArticlesService
  private val client: ArticlesClient = mock()

  @ExperimentalCoroutinesApi
  @get:Rule
  var coroutinesRule = MainCoroutinesRule()

  @Before
  fun initService() {
    service = createService(ArticlesService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchArticleListFromNetworkTest() = runBlocking {
    enqueueResponse("/ArticlesResponse.json")
    val response = service.fetchArticlesList(Constants.BASE_URL)
    val responseBody = requireNotNull((response as ApiResponse.Success).data)
    mockWebServer.takeRequest()

    client.fetchArticlesList()
    assertThat(responseBody.status, `is`("OK"))
    assertThat(responseBody.copyRight, `is`("Copyright (c) 2020 The New York Times Company.  All Rights Reserved."))
    assertThat(responseBody.num_results, `is`(20))
    assertThat(responseBody.results[0].title, `is`("The ‘Red Slime’ Lawsuit That Could Sink Right-Wing Media"))
    assertThat(responseBody.results[0].url, `is`("https://www.nytimes.com/2020/12/20/business/media/smartmatic-lawsuit-fox-news-newsmax-oan.html"))
  }

}
