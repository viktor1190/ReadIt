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
import timber.log.Timber
import javax.inject.Inject

/**
 * Limited to 100 items
 */
private const val ITEMS_PER_PAGE = 25

@HiltViewModel
class TopRedditPostsViewModel @Inject constructor(
    // TODO private val dispatcherProvider: DispatcherProvider,
    private val redditPostsRepository: TopPostsPagingSourceRepository
) : BaseViewModel(), DataLoaderCallbackPageAdapter {

    val topRedditPosts: Flow<PagingData<RedditPost>> = Pager(
        PagingConfig(pageSize = ITEMS_PER_PAGE)
    ) {
        redditPostsRepository.getTopPostsPagingSource()
    }.flow.cachedIn(viewModelScope)

    fun navigateToSecondFragment() {
        Timber.v("Navigating to the second fragment...")
        // TODO
        // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun submitAdapterPageData(adapter: TopRedditPostsPagingAdapter, pagingData: PagingData<RedditPost>) {
        viewModelScope.launch {
            adapter.submitData(pagingData)
        }
    }

}