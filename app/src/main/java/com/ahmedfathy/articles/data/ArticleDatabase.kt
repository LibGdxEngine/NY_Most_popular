package com.ahmedfathy.articles.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedfathy.articles.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    class Callback @Inject constructor(
        private val database: Provider<ArticleDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().articleDao()

            applicationScope.launch {
//                dao.insert(NyArticle("Wash the dishes"))
//                dao.insert(NyArticle("Do the laundry"))
//                dao.insert(NyArticle("Buy groceries", readed = true))
//                dao.insert(NyArticle("Prepare food", completed = true))
//                dao.insert(NyArticle("Call mom"))
//                dao.insert(NyArticle("Visit grandma", completed = true))
//                dao.insert(NyArticle("Repair my bike"))
//                dao.insert(NyArticle("Call Elon Musk"))
            }
        }
    }
}