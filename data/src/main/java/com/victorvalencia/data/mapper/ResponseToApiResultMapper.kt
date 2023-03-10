package com.victorvalencia.data.mapper

import com.squareup.moshi.JsonDataException
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.ServerError
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

internal interface ResponseToApiResultMapper {
    /** Convert from response to [ApiResult] of the response */
    fun <T : Any> toResult(response: Response<T>): ApiResult<T>

    /** Convert from response to [ApiResult] of Unit of the response. Use when you don't care about the actual success type. */
    fun <T : Any> toEmptyResult(response: Response<T>): ApiResult<Unit>
}

class ResponseToApiResultMapperImpl @Inject constructor(): ResponseToApiResultMapper {

    override fun <T : Any> toResult(response: Response<T>): ApiResult<T> {
        return when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    Timber.w("[toResult] Response body null")
                    ApiResult.Failure.GeneralFailure("null response body")
                }
            }
            else -> {
                Timber.w("[toResult] Api not successful: message ${response.message()} code: ${response.code()}")
                ApiResult.Failure.Server(generateServerError(response))
            }
        }
    }

    override fun <T : Any> toEmptyResult(response: Response<T>): ApiResult<Unit> {
        return when {
            response.isSuccessful -> ApiResult.Success(Unit)
            else -> {
                Timber.w("[toEmptyResult] Api not successful: message ${response.message()} code: ${response.code()}")
                ApiResult.Failure.Server(generateServerError(response))
            }
        }
    }

    /** Determines the correct error response format, deserializes it, and converts it to a [ServerErrorDto] */
    private fun <T> generateServerError(response: Response<T>): ServerError {
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) throw JsonDataException()
        return ServerError(httpErrorCode = response.code(), status = response.message())
    }
}