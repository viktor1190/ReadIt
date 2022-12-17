package com.victorvalencia.data.network

import com.victorvalencia.data.model.response.RedditTopPostsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RedditService {

    @GET(value = "top.json")
    suspend fun getTopListAfter(
        @Query(value = "after") id: String?,
        @Query(value = "count") count: Int? = 0,
        @Query(value = "limit") limit: Int? = 25,
    ): Response<RedditTopPostsResponse>

    @GET(value = "top.json")
    suspend fun getTopListBefore(
        @Query(value = "before") id: String?,
        @Query(value = "count") count: Int? = 0,
        @Query(value = "limit") limit: Int? = 25,
    ): Response<RedditTopPostsResponse>
}