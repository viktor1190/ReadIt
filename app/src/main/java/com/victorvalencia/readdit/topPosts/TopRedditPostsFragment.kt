package com.victorvalencia.readdit.topPosts

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorvalencia.readdit.R
import com.victorvalencia.readdit.base.BaseFragment
import com.victorvalencia.readdit.databinding.FragmentTopRedditPostsBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TopRedditPostsFragment : BaseFragment<TopRedditPostsViewModel, FragmentTopRedditPostsBinding>() {

    override val fragmentViewModel: TopRedditPostsViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_top_reddit_posts
}