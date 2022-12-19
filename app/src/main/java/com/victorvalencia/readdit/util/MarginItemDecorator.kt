package com.victorvalencia.readdit.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This decorator helps to apply a consistent margin across the items of the RecycleView
 * This decorator only supports LinearLayoutManager.
 */
class MarginItemDecorator(
    private val marginStartInPixels: Int,
    private val marginEndInPixels: Int,
    private val spaceBetweenInPixels: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            throw IllegalArgumentException("MarginHorizontalItemsDecorator doesn't supports the current layout manager. Change it or remove the decorator")
        }

        with(outRect) {
            val childAdapterPosition = parent.getChildAdapterPosition(view)
            val lastItemPosition = (parent.adapter?.itemCount ?: 0) - 1
            if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                left = if (childAdapterPosition == 0) marginStartInPixels else spaceBetweenInPixels / 2
                right = if (childAdapterPosition == lastItemPosition) marginEndInPixels else spaceBetweenInPixels / 2
            } else {
                top = if (childAdapterPosition == 0) marginStartInPixels else spaceBetweenInPixels / 2
                bottom = if (childAdapterPosition == lastItemPosition) marginEndInPixels else spaceBetweenInPixels / 2
            }
        }
    }
}
