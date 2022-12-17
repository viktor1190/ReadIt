package com.victorvalencia.data.model.domain

data class TopPostPaging(
    val nextId: String?,
    val prevId: String?,
    val lastCount: Int?,
    val posts: List<RedditPost>
)