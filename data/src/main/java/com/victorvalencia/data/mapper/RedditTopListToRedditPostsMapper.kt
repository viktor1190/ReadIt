package com.victorvalencia.data.mapper

import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.asSuccess
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.data.model.domain.TopPostPaging
import com.victorvalencia.data.model.response.RedditTopPostsResponse

internal fun RedditTopPostsResponse.mapToTopPostPaging(): ApiResult<TopPostPaging> {
    return TopPostPaging(
        nextId = data.after,
        prevId = data.before,
        lastCount = data.dist,
        posts = data.children
            .map {
                RedditPost(
                    id = it.data.id,
                    title = it.data.title,
                    subreddit = it.data.subreddit,
                    thumbnailUrl = it.data.thumbnailUrl,
                    permalink = it.data.permalink,
                    url = it.data.url,
                    isVideo = it.data.isVideo ?: false,
                    pagingId = it.data.name
                )
            }
    ).asSuccess()
}