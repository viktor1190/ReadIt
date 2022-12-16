package com.victorvalencia.data.network

import com.victorvalencia.data.mapper.mapToRedditPosts
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.ResponseToApiResultMapper
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.map
import com.victorvalencia.data.model.wrapExceptions
import retrofit2.Response
import javax.inject.Inject

internal interface RedditApi {

    suspend fun getTopList(): ApiResult<List<RedditPost>>
}

internal class RedditApiImpl @Inject constructor(
    private val redditService: RedditService,
    private val responseToApiResultMapper: ResponseToApiResultMapper,
    private val networkHandler: NetworkHandler
): RedditApi {

    override suspend fun getTopList(): ApiResult<List<RedditPost>> {
        return wrapExceptions("getTopList") {
            redditService.getTopList().toResult()
        }.map { dto ->
            dto.mapToRedditPosts()
        }

    }

    /** Delegates to [wrapExceptions], passing in the class name here instead of requiring it of all callers */
    private suspend fun <T : Any> wrapExceptions(methodName: String, block: suspend () -> ApiResult<T>): ApiResult<T> {
        return checkNetworkBefore { wrapExceptions("RedditApiImpl", methodName, block) }
    }

    private fun <T : Any> Response<T>.toResult(): ApiResult<T> {
        return responseToApiResultMapper.toResult(this)
    }

    private suspend fun <T : Any> checkNetworkBefore(block: suspend () -> ApiResult<T>): ApiResult<T> {
        if (networkHandler.isConnected != true) {
            return ApiResult.Failure.NetworkTimeoutFailure(exception = null, noConnection = true)
        }
        return block()
    }
}