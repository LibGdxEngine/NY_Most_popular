package com.ahmedfathy.articles.di

import android.app.Application
import androidx.room.Room
import com.ahmedfathy.articles.data.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: ArticleDatabase.Callback
    ) = Room.databaseBuilder(app, ArticleDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideArticleDao(db: ArticleDatabase) = db.articleDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope