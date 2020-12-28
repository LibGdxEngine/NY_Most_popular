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

package com.skydoves.pokedex.persistence

import android.util.Log
import androidx.lifecycle.asLiveData
import com.ahmedfathy.articles.data.ArticleDao
import com.ahmedfathy.articles.data.SortOrder
import com.ahmedfathy.articles.mapper.mapper.fromModelToEntity
import com.skydoves.pokedex.utils.MockUtil.mockArticle
import com.skydoves.pokedex.utils.MockUtil.mockArticleList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class ArticleDaoTest : LocalDatabase() {

  private lateinit var pokemonDao: ArticleDao

  @Before
  fun init() {
    pokemonDao = db.articleDao()
  }

  @Test
  fun insertAndLoadArticleListTest() = runBlocking {
    //arrange
    val mockDataList = mockArticleList()
    var newArticle = fromModelToEntity(mockDataList.get(0))
    var firstDbItemID : Long = -1
    //act
    pokemonDao.insert(newArticle)

    pokemonDao.getArticles("", SortOrder.BY_DATE , false).collect {
      firstDbItemID = it.get(0).id!!
    }
    //assert
    val mockData = mockArticle()
    assertThat(firstDbItemID.toString(), `is`(mockData.id.toString()))
  }
}
