package com.victorvalencia.readdit.topPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.victorvalencia.data.model.domain.RedditPost
import com.victorvalencia.readdit.BR

interface DataLoaderCallbackPageAdapter {
    fun submitAdapterPageData(topRedditPostsPagingAdapter: TopRedditPostsPagingAdapter, pagingData: PagingData<RedditPost>)
}

class TopRedditPostsPagingAdapter(
    private val parentLifecycleOwner: LifecycleOwner,
    @LayoutRes private val layoutRes: Int,
): PagingDataAdapter<RedditPost, RedditPostViewHolder>(RedditPostComparator) {

    override fun onBindViewHolder(holder: RedditPostViewHolder, position: Int) {
        val item: RedditPost? = getItem(position)
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutRes, parent, false)
        return RedditPostViewHolder(binding, parentLifecycleOwner)
    }

    override fun onViewRecycled(holder: RedditPostViewHolder) {
        holder.onVisible()
    }

    override fun onViewAttachedToWindow(holder: RedditPostViewHolder) {
        holder.onVisible()
        holder.updateView()
    }

    override fun onViewDetachedFromWindow(holder: RedditPostViewHolder) {
        holder.onInvisible()
    }
}

object RedditPostComparator: DiffUtil.ItemCallback<RedditPost>() {

    override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem == newItem
    }

}

class RedditPostViewHolder(
    private val binding: ViewDataBinding,
    private val parentLifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry = RecyclerViewLifecycleRegistry(this, parentLifecycleOwner.lifecycle)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun updateView() {
        binding.invalidateAll()
    }

    fun onVisible() {
        lifecycleRegistry.highestState = Lifecycle.State.STARTED
    }

    fun onInvisible() {
        lifecycleRegistry.highestState = Lifecycle.State.CREATED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun bind(redditPost: RedditPost?) {
        with(binding) {
            lifecycleOwner = this@RedditPostViewHolder
            setVariable(BR.redditPost, redditPost)
            executePendingBindings()
        }
    }
}

/**
 * This class was written based on [this article](https://medium.com/@stephen.brewer/an-adventure-with-recyclerview-databinding-livedata-and-room-beaae4fc8116)
 */
class RecyclerViewLifecycleRegistry(private val owner: LifecycleOwner, private val parent: Lifecycle) : LifecycleRegistry(owner) {
    private val parentLifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Event.ON_ANY)
        fun onAny() {
            currentState = parent.currentState
        }
    }
    var highestState = State.INITIALIZED
        set(value) {
            field = value
            if (parent.currentState >= value) {
                currentState = value
            }
        }

    init {
        observeParent()
    }

    private fun observeParent() {
        parent.addObserver(parentLifecycleObserver)
    }

    private fun ignoreParent() {
        parent.removeObserver(parentLifecycleObserver)
    }

    override fun setCurrentState(nextState: State) {
        val maxNextState = if (nextState > highestState)
            highestState else nextState
        if (nextState == State.DESTROYED) {
            ignoreParent()
        }
        super.setCurrentState(maxNextState)
    }
}