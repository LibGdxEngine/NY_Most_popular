package com.ahmedfathy.articles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    //used as higher level api for getting sorted articles
    fun getArticles(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<ArticleEntity>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }
    //get articles sorted with title
    @Query("SELECT * FROM articles_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY title")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<ArticleEntity>>
    //get articles sorted with publishing date
    @Query("SELECT * FROM articles_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY publishedDate")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<ArticleEntity>>
    //insert new article in database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articleEntity: ArticleEntity)
    //update existing article
    @Update
    suspend fun update(articleEntity: ArticleEntity)

}