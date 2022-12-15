package com.victorvalencia.data.network

import com.victorvalencia.data.model.response.RedditTopPostsResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface RedditService {

    @GET(value = "top.json")
    suspend fun getTopList(): Response<RedditTopPostsResponse>
}