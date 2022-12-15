package com.victorvalencia.data.model.domain

data class RedditPost(
    val title: String,
    val subreddit: String,
    val thumbnailUrl: String,
    val permalink: String,
    val url: String,
    val isVideo: Boolean
)