package com.ahmedfathy.articles.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class Article (
    @field:Json(name = "uri")val uri: String,
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
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Media (
    @field:Json(name = "type") val type: String,
    @field:Json(name = "subtype")val subtype: String,
    @field:Json(name = "caption")val caption: String,
    @field:Json(name = "copyright")val copyright: String,
    @field:Json(name = "approved_for_syndication")val approvedForSyndication: Long,
    @field:Json(name = "media-metadata")val mediaMetadata: List<MediaMetadatum>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class MediaMetadatum (
    @field:Json(name = "url")val url: String,
    @field:Json(name = "format")val format: String,
    @field:Json(name = "height")val height: Long,
    @field:Json(name = "width")val width: Long
) : Parcelable

