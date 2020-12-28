package com.ahmedfathy.articles.di

import com.ahmedfathy.articles.network.ArticlesClient
import com.ahmedfathy.articles.network.ArticlesService
import com.ahmedfathy.articles.network.HttpRequestInterceptor
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2000 ,TimeUnit.MILLISECONDS)
            .readTimeout(2000 , TimeUnit.MILLISECONDS)
            .writeTimeout(2000 , TimeUnit.MILLISECONDS)
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/mostpopular/v2/mostviewed/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleService(retrofit: Retrofit): ArticlesService {
        return retrofit.create(ArticlesService::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleClient(articleService: ArticlesService): ArticlesClient {
        return ArticlesClient(articleService)
    }
}