package com.victorvalencia.data

import androidx.paging.PagingSource
import com.victorvalencia.data.model.domain.RedditPost
import javax.inject.Inject

interface TopPostsPagingSourceRepository {

    fun getTopPostsPagingSource(): PagingSource<String, RedditPost>
}

internal class TopPostsPagingSourceRepositoryImpl @Inject constructor(
    private val redditPostsRepository: RedditPostsRepository
) : TopPostsPagingSourceRepository {

    override fun getTopPostsPagingSource(): PagingSource<String, RedditPost> {
        return RedditPostPagingSource(redditPostsRepository)
    }
}