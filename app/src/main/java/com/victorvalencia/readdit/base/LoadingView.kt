package com.victorvalencia.readdit.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.victorvalencia.readdit.R

class LoadingView constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attributeSet, defStyleAttr
) {

    init {
        View.inflate(context, R.layout.view_all_loading, this)
    }
}
