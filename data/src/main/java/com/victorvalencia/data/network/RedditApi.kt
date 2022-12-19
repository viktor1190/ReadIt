package com.victorvalencia.data.network

import com.victorvalencia.data.mapper.mapToTopPostPaging
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.mapper.ResponseToApiResultMapper
import com.victorvalencia.data.model.domain.TopPostPaging
import com.victorvalencia.data.model.map
import com.victorvalencia.data.model.wrapExceptions
import retrofit2.Response
import javax.inject.Inject

internal interface RedditApi {

    suspend fun getNextTopList(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging>

    suspend fun getPrevTopList(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging>
}

internal class RedditApiImpl @Inject constructor(
    private val redditService: RedditService,
    private val responseToApiResultMapper: ResponseToApiResultMapper,
    private val networkHandler: NetworkHandler
): RedditApi {

    override suspend fun getNextTopList(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging> {
        return wrapExceptions("getTopList") {
            redditService.getTopListAfter(anchorId, count, limit).toResult()
        }.map { dto ->
            dto.mapToTopPostPaging()
        }

    }

    override suspend fun getPrevTopList(anchorId: String?, count: Int?, limit: Int?): ApiResult<TopPostPaging> {
        return wrapExceptions("getTopList") {
            redditService.getTopListBefore(anchorId, count, limit).toResult()
        }.map { dto ->
            dto.mapToTopPostPaging()
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