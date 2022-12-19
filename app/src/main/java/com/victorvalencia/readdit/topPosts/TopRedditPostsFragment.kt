package com.victorvalencia.readdit.topPosts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorvalencia.readdit.R
import com.victorvalencia.readdit.base.BaseFragment
import com.victorvalencia.readdit.base.MarginItemDecorator
import com.victorvalencia.readdit.databinding.FragmentTopRedditPostsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class TopRedditPostsFragment : BaseFragment<TopRedditPostsViewModel, FragmentTopRedditPostsBinding>() {

    override val fragmentViewModel: TopRedditPostsViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_top_reddit_posts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val redditPostsAdapter = TopRedditPostsPagingAdapter(viewLifecycleOwner, R.layout.item_top_reddit_post)

        binding.recyclerviewRedditPosts.apply {
            adapter = redditPostsAdapter
            layoutManager = LinearLayoutManager(context)
            val decoration = MarginItemDecorator(
                marginStartInPixels = resources.getDimensionPixelSize(R.dimen.margin_all_medium),
                marginEndInPixels = resources.getDimensionPixelSize(R.dimen.margin_all_medium),
                spaceBetweenInPixels = 0
            )
            addItemDecoration(decoration)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                redditPostsAdapter.loadStateFlow.collect {
                    showLoading(it.source.prepend is LoadState.Loading)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fragmentViewModel.topRedditPosts.collect {
                    redditPostsAdapter.submitData(it)
                    binding.swipeRefreshTopRedditPosts.isRefreshing = false
                }
            }
        }
    }
}