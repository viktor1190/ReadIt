package com.victorvalencia.readdit.topPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorvalencia.readdit.R
import com.victorvalencia.readdit.base.BaseFragment
import com.victorvalencia.readdit.databinding.FragmentTopRedditPostsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class TopRedditPostsFragment : BaseFragment<TopRedditPostsViewModel, FragmentTopRedditPostsBinding>() {

    override val fragmentViewModel: TopRedditPostsViewModel by viewModels()

    override fun getLayoutResource() = R.layout.fragment_top_reddit_posts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentViewModel.getRedditPosts()
    }
}