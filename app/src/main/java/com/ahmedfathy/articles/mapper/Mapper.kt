package com.ahmedfathy.articles.mapper

import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.models.Article
import java.lang.Exception

object mapper {
    //Convert Article Model to Article Entity model
    fun fromModelToEntity(article:Article) : ArticleEntity {
        var imageUrl:String? = ""
        var thumbnail:String? = ""
        try{
            imageUrl = article.media.get(0).mediaMetadata.get(2).url
            thumbnail = article.media.get(0).mediaMetadata.get(0).url
        }catch (ex : Exception){

        }
        var newArticle = ArticleEntity(
            id = article.id,
            title = article.title,
            url = article.url,
            source = article.source,
            section = article.section + "  " + article.subsection,
            publishedDate = article.publishedDate,
            imageUrl = imageUrl,
            thumbnail = thumbnail
        )
        return newArticle
    }
}