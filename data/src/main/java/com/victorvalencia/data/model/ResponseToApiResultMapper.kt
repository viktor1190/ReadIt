package com.victorvalencia.data.model

import retrofit2.Response

internal interface ResponseToApiResultMapper {
    /** Convert from response to [ApiResult] of the response */
    fun <T : Any> toResult(response: Response<T>): ApiResult<T>

    /** Convert from response to [ApiResult] of Unit of the response. Use when you don't care about the actual success type. */
    fun <T : Any> toEmptyResult(response: Response<T>): ApiResult<Unit>
}