package com.victorvalencia.data

import androidx.paging.PagingSource
import com.victorvalencia.data.model.domain.RedditPost
import javax.inject.Inject

interface TopPostsPagingSourceRepository {

    fun getTopPostsPagingSource(): PagingSource<String, RedditPost>
}

internal class TopPostsPagingSourceRepositoryImpl @Inject constructor(
    private val redditPostsPagingSource: PagingSource<String, RedditPost>
) : TopPostsPagingSourceRepository {

    override fun getTopPostsPagingSource(): PagingSource<String, RedditPost> {
        return redditPostsPagingSource
    }
}