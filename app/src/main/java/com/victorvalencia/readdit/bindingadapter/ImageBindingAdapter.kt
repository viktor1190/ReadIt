package com.victorvalencia.readdit.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.victorvalencia.readdit.topPosts.TopRedditPostsPagingAdapter

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(this)
            .load(imageUrl)
            .error(drawable)
            .into(this)
    }
}

@BindingAdapter("refreshListAdapter")
fun SwipeRefreshLayout.refreshListAdapter(view: View) {
    if (view is RecyclerView) {
        setOnRefreshListener {
            isRefreshing = true
            (view.adapter as TopRedditPostsPagingAdapter).refresh()
        }
    }
}