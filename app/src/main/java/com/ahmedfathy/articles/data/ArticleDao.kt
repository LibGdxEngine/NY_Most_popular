package com.ahmedfathy.articles.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    fun getArticles(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<ArticleEntity>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM articles_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY readed DESC, title")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY readed DESC, publishedDate")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articleEntity: ArticleEntity)

    @Update
    suspend fun update(articleEntity: ArticleEntity)

    @Delete
    suspend fun delete(articleEntity: ArticleEntity)

    @Query("DELETE FROM articles_table WHERE readed = 1")
    suspend fun deleteCompletedTasks()
}