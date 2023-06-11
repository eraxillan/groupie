package com.xwray.groupie

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

public abstract class TouchCallback1 : ItemTouchHelper.SimpleCallback(0, 0) {

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return (viewHolder as? GroupieViewHolder)?.swipeDirs ?: 0
    }

    override fun getDragDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return (viewHolder as? GroupieViewHolder)?.dragDirs ?: 0
    }
}