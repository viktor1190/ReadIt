package com.victorvalencia.data.di

import androidx.paging.PagingSource
import com.squareup.moshi.Moshi
import com.victorvalencia.data.RedditPostPagingSource
import com.victorvalencia.data.RedditPostsRepository
import com.victorvalencia.data.RedditPostsRepositoryImpl
import com.victorvalencia.data.TopPostsPagingSourceRepository
import com.victorvalencia.data.TopPostsPagingSourceRepositoryImpl
import com.victorvalencia.data.model.ResponseToApiResultMapper
import com.victorvalencia.data.model.ResponseToApiResultMapperImpl
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.network.NetworkHandler
import com.victorvalencia.data.network.NetworkHandlerImpl
import com.victorvalencia.data.network.RedditApi
import com.victorvalencia.data.network.RedditApiImpl
import com.victorvalencia.data.network.RedditService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @NetworkServiceBaseUrl
    @Provides
    fun providesBaseUrl(): String = "https://reddit.com"

    @Provides
    internal fun providesRedditService(
        @NetworkServiceBaseUrl baseUrl: String,
        httpClient: OkHttpClient
    ): RedditService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .build()
                )
            )
            .build()
            .create(RedditService::class.java)
    }

    @Provides
    fun providesHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkServiceBaseUrl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindRedditRepository(redditRepositoryImpl: RedditPostsRepositoryImpl): RedditPostsRepository

    @Binds
    internal abstract fun bindtopPostsPagingSourceRepository(
        topPostsPagingSourceRepositoryImpl: TopPostsPagingSourceRepositoryImpl
    ): TopPostsPagingSourceRepository

    @Binds
    internal abstract fun bindRedditApi(redditApiImpl: RedditApiImpl): RedditApi

    @Binds
    internal abstract fun bindResponseToApiResultMapper(responseToApiResultMapperImpl: ResponseToApiResultMapperImpl): ResponseToApiResultMapper

    @Binds
    abstract fun bindNetworkHandler(networkHandlerImpl: NetworkHandlerImpl): NetworkHandler

}