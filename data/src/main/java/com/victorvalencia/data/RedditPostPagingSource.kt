package com.victorvalencia.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.asNetworkFailureException
import com.victorvalencia.data.model.domain.RedditPost

class RedditPostPagingSource(
    private val redditPostsRepository: RedditPostsRepository
) : PagingSource<String, RedditPost>() {

    private var lastCount: Int? = null

    override fun getRefreshKey(state: PagingState<String, RedditPost>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPost> {
        val result = when (params) {
            is LoadParams.Refresh -> redditPostsRepository
                .getNextTopPosts(anchorId = null, count = 0, limit = params.loadSize)
            is LoadParams.Append -> redditPostsRepository
                .getNextTopPosts(anchorId = params.key, count = lastCount, limit = params.loadSize)
            is LoadParams.Prepend -> redditPostsRepository
                .getPrevTopPosts(anchorId = params.key, count = 0, limit = params.loadSize)
        }
        return when (result) {
            is ApiResult.Success -> {
                lastCount = result.data.lastCount
                LoadResult.Page(
                    data = result.data.posts,
                    prevKey = result.data.prevId,
                    nextKey = result.data.nextId
                )
            }
            is ApiResult.Failure -> LoadResult.Error(result.asNetworkFailureException())
        }
    }
}