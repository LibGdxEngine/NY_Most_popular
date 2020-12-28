package com.ahmedfathy.articles.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "articles_table")
@Parcelize
data class ArticleEntity(
    var url : String? = "",
    var title: String?,
    var source: String? = "",
    var section: String? = "",
    var completed: Boolean = false,
    var publishedDate: String? = "",
    var imageUrl : String? = "",
    var thumbnail: String? = "",
    @PrimaryKey(autoGenerate = false) var id: Long? = 0
) : Parcelable {


}

