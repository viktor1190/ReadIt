package com.victorvalencia.data

import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.network.RedditApi

interface RedditRepository {

    suspend fun getTopPosts(): ApiResult<List<RedditPost>>
}

internal class RedditRepositoryImpl(private val redditApi: RedditApi): RedditRepository {

    override suspend fun getTopPosts(): ApiResult<List<RedditPost>> {
        return redditApi.getTopList()
    }
}