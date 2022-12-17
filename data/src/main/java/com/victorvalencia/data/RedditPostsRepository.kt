package com.victorvalencia.data

import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.TopPostPaging
import com.victorvalencia.data.network.RedditApi
import javax.inject.Inject

interface RedditPostsRepository {

    suspend fun getNextTopPosts(anchorId: String? = null, count: Int?, limit: Int?): ApiResult<TopPostPaging>

    suspend fun getPrevTopPosts(anchorId: String? = null, count: Int?, limit: Int?): ApiResult<TopPostPaging>
}

internal class RedditPostsRepositoryImpl @Inject constructor(
    private val redditApi: RedditApi
    ): RedditPostsRepository {

    override suspend fun getNextTopPosts(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging> {
        return redditApi.getNextTopList(anchorId, count, limit)
    }

    override suspend fun getPrevTopPosts(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging> {
        return redditApi.getPrevTopList(anchorId, count, limit)
    }
}