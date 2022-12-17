package com.victorvalencia.data.model.domain
// TODO update the package name to containt victorvalencia.readdit.data...

data class RedditPost(
    val id: String,
    val title: String,
    val subreddit: String,
    val author: String,
    val thumbnailUrl: String,
    val permalink: String,
    val url: String,
    val isVideo: Boolean,
    val pagingId: String
)