package com.sandisetiawan444.core.util

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min


/**
 * Created by Sandi on 24/02/2020.
 */

class ZoomLinearLayoutManager : LinearLayoutManager {

    private val mShrinkAmount = 0.15f
    private val mShrinkDistance = 0.50f

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to vertical orientation.
     *
     * [android.R.attr.orientation]
     * [androidx.recyclerview.R.attr.reverseLayout]
     * [androidx.recyclerview.R.attr.stackFromEnd]
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        val properties =
            RecyclerView.LayoutManager.getProperties(
                context,
                attrs,
                defStyleAttr,
                defStyleRes
            )
        orientation = properties.orientation
        reverseLayout = properties.reverseLayout
        stackFromEnd = properties.stackFromEnd
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChildren()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        scaleChildren()
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        scrollVerticallyBy(0, recycler, state)
    }

    private fun scaleChildren() {
        val midpoint = width / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount

        for (i in 0 until childCount) {
            getChildAt(i)?.apply {
                val childMidpoint = (getDecoratedRight(this) + getDecoratedLeft(this)) / 2f
                val d = min(d1, abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

                scaleX = scale
                scaleY = scale
            }
        }
    }
}