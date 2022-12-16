package com.victorvalencia.data.mapper

import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.asSuccess
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.response.RedditTopPostsResponse

internal fun RedditTopPostsResponse.mapToRedditPosts(): ApiResult<List<RedditPost>> {
    return data.children.map {
        RedditPost(
            it.data.title,
            it.data.subreddit,
            it.data.thumbnailUrl,
            it.data.permalink,
            it.data.url,
            it.data.isVideo ?: false
        )
    }.asSuccess()
}