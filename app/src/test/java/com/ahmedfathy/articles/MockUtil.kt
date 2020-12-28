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

package com.skydoves.pokedex.utils

import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.models.Article
import com.ahmedfathy.articles.models.Media
import com.ahmedfathy.articles.models.MediaMetadatum

object MockUtil {
/*
*     @field:Json(name = "uri")val uri: String,
    @field:Json(name = "url")val url: String,
    @field:Json(name = "id")val id: Long,
    @field:Json(name = "asset_id")val assetID: Long,
    @field:Json(name = "source")val source: String,
    @field:Json(name = "published_date")val publishedDate: String,
    @field:Json(name = "updated")val updated: String,
    @field:Json(name = "section")val section: String,
    @field:Json(name = "subsection")val subsection: String,
    @field:Json(name = "nytdsection")val nytdsection: String,
    @field:Json(name = "adx_keywords")val adxKeywords: String,
    @field:Json(name = "column")val column: String?,
    @field:Json(name = "byline")val byline: String,
    @field:Json(name = "type")val type: String,
    @field:Json(name = "title")val title: String,
    @field:Json(name = "abstract")val abstract: String,
    @field:Json(name = "des_facet")val desFacet: List<String>,
    @field:Json(name = "org_facet")val orgFacet: List<String>,
    @field:Json(name = "per_facet")val perFacet: List<String>,
    @field:Json(name = "geo_facet")val geoFacet: List<String>,
    @field:Json(name = "media")val media: List<Media>,
    @field:Json(name = "eta_id")val etaID: Long
* */
  fun mockArticle() = Article(
    uri =  "",
    url = "https://www.nytimes.com/2020/12/22/world/americas/skylar-mack-cayman-islands-covid.html",
    id = 100000007516155,
    assetID = 100,
    source = "New York Times",
    publishedDate = "2020-12-22",
    updated = "",
    section = "World",
    subsection = "",
    nytdsection = "",
    adxKeywords = "",
    column = null,
    byline = "",
    type = "",
    title = "Sentence Is Cut for U.S. Student Who Broke Quarantine",
    abstract = "",
    desFacet = listOf("String"),
    orgFacet = listOf("String"),
    perFacet = listOf("String"),
    geoFacet = listOf("String"),
    media = listOf(Media("" , "" , "" , "" ,100 , listOf(MediaMetadatum("" , "" , 75 ,75)))),
    etaID = 100
  )

  fun mockArticleList() = listOf(mockArticle())

}
