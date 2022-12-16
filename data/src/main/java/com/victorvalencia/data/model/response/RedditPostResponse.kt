package com.victorvalencia.data.model.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
internal data class RedditTopPostsResponse(
    @Json(name = "data") val data: RedditListDataResponse
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
internal data class RedditListDataResponse(
    @Json(name = "children") val children: List<RedditChildrenItemsResponse>
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
internal data class RedditChildrenItemsResponse(
    @Json(name = "data") val data: RedditTopItemResponse
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
internal data class RedditTopItemResponse(
    @Json(name = "title") val title: String,
    @Json(name = "subreddit") val subreddit: String,
    @Json(name = "thumbnail") val thumbnailUrl: String,
    @Json(name = "permalink") val permalink: String,
    @Json(name = "url") val url: String,
    @Json(name = "isVideo") val isVideo: Boolean?
): Parcelable