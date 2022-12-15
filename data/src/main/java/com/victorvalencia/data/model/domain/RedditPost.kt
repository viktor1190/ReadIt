package com.victorvalencia.data.model.domain
// TODO update the package name to containt victorvalencia.readdit.data...

data class RedditPost(
    val title: String,
    val subreddit: String,
    val thumbnailUrl: String,
    val permalink: String,
    val url: String,
    val isVideo: Boolean
)