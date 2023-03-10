package com.victorvalencia.readdit.topPosts

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.victorvalencia.data.TopPostsPagingSourceRepository
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.readdit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Limited to 100 items
 */
private const val ITEMS_PER_PAGE = 25

@HiltViewModel
class TopRedditPostsViewModel @Inject constructor(
    private val redditPostsRepository: TopPostsPagingSourceRepository
) : BaseViewModel(), DataLoaderCallbackPageAdapter {

    val topRedditPosts: Flow<PagingData<RedditPost>> = Pager(
        PagingConfig(pageSize = ITEMS_PER_PAGE)
    ) {
        redditPostsRepository.getTopPostsPagingSource()
    }.flow.cachedIn(viewModelScope)

    override fun submitAdapterPageData(topRedditPostsPagingAdapter: TopRedditPostsPagingAdapter, pagingData: PagingData<RedditPost>) {
        viewModelScope.launch {
            topRedditPostsPagingAdapter.submitData(pagingData)
        }
    }

}