package com.victorvalencia.readdit.topPosts

import androidx.lifecycle.viewModelScope
import com.victorvalencia.data.RedditRepository
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.readdit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopRedditPostsViewModel @Inject constructor(
    // TODO private val dispatcherProvider: DispatcherProvider,
    private val redditRepository: RedditRepository
) : BaseViewModel() {

    val topRedditPosts: StateFlow<List<RedditPost>> = MutableStateFlow(emptyList())
    val testingText: StateFlow<String> = MutableStateFlow("Empty data")

    fun getRedditPosts() {
        viewModelScope.launch/* TODO (dispatcherProvider.IO)*/ {
            when (val result = wrapWithLoadingAndErrorEvents { redditRepository.getTopPosts() }) {
                is ApiResult.Success -> topRedditPosts.set(result.data)
                is ApiResult.Failure -> { TODO() }
            }
        }
    }

    fun navigateToSecondFragment() {
        Timber.v("Navigating to the second fragment...")
        // TODO
        // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

    }

}