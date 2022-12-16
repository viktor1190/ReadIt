package com.victorvalencia.readdit.topPosts

import androidx.lifecycle.viewModelScope
import com.victorvalencia.data.RedditRepository
import com.victorvalencia.data.model.ApiResult
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.readdit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopRedditPostsViewModel @Inject constructor(
    // TODO private val dispatcherProvider: DispatcherProvider,
    private val redditRepository: RedditRepository
) : BaseViewModel() {

    val topRedditPosts: StateFlow<List<RedditPost>> = MutableStateFlow(emptyList())
    val testingText: StateFlow<String> = topRedditPosts.map { "Top posts received = ${it.size}" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "Loading...")

    fun getRedditPosts() {
        viewModelScope.launch/* TODO (dispatcherProvider.IO)*/ {
            when (val result = wrapWithLoadingAndErrorEvents { redditRepository.getTopPosts() }) {
                is ApiResult.Success -> topRedditPosts.set(result.data)
                is ApiResult.Failure -> { Timber.e("Error was received while trying to fetch the Reddit Tops: $result") }
            }
        }
    }

    fun navigateToSecondFragment() {
        Timber.v("Navigating to the second fragment...")
        // TODO
        // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

    }

}