package com.ahmedfathy.articles.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//response that we receive from api
@JsonClass(generateAdapter = true)
data class ArticleResponse (
    @field:Json(name = "status") val status: String,
    @field:Json(name = "copyright") val copyRight: String?,
    @field:Json(name = "num_results") val num_results: Long,
    @field:Json(name = "results") val results: List<Article>
)
