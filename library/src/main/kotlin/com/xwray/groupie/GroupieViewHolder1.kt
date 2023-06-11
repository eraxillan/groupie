package com.xwray.groupie

import androidx.recyclerview.widget.RecyclerView
import android.view.View

public open class GroupieViewHolder1(rootView: View) : RecyclerView.ViewHolder(rootView) {
    private var item: Item1<*>? = null
    private var onItemClickListener: OnItemClickListener1? = null
    private var onItemLongClickListener: OnItemLongClickListener1? = null

    private val onClickListener = View.OnClickListener { v ->
        // Discard click if the viewholder has been removed, but was still in the process of
        // animating its removal while clicked (unlikely, but technically possible)
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            onItemClickListener?.onItemClick(item ?: return@OnClickListener, v)
        }
    }

    private val onLongClickListener = View.OnLongClickListener { v ->
        // Discard long click if the viewholder has been removed, but was still in the process of
        // animating its removal while long clicked (unlikely, but technically possible)
        return@OnLongClickListener if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            val item = item ?: return@OnLongClickListener false
            onItemLongClickListener?.onItemLongClick(item, v) == true
        } else {
            false
        }
    }

    fun bind(
        item: Item1<*>,
        onItemClickListener: OnItemClickListener1?,
        onItemLongClickListener: OnItemLongClickListener1?
    ) {
        this.item = item

        // Only set the top-level click listeners if a) they exist, and b) the item has
        // clicks enabled.  This ensures we don't interfere with user-set click listeners.

        // It would be nice to keep our listeners always attached and set them only once on creating
        // the viewholder, but different items of the same layout type may not have the same click
        // listeners or even agree on whether they are clickable.
        if (onItemClickListener != null && item.isClickable()) {
            itemView.setOnClickListener(onClickListener)
            this.onItemClickListener = onItemClickListener
        }

        if (onItemLongClickListener != null && item.isLongClickable()) {
            itemView.setOnLongClickListener(onLongClickListener)
            this.onItemLongClickListener = onItemLongClickListener
        }
    }

    fun unbind() {
        // Only set the top-level click listener to null if we had previously set it ourselves.

        // This avoids undoing any click listeners the user may set which might be persistent for
        // the life of the viewholder. (It's up to the user to make sure that's correct behavior.)
        if (onItemClickListener != null && item?.isClickable() == true) {
            itemView.setOnClickListener(null)
        }
        if (onItemLongClickListener != null && item?.isLongClickable() == true) {
            itemView.setOnClickListener(null)
        }
        item = null
        onItemClickListener = null
        onItemLongClickListener = null
    }

    fun getExtras(): Map<String, Any> = item?.getExtras() ?: emptyMap()
    fun getSwipeDirs(): Int = item?.getSwipeDirs() ?: 0
    fun getDragDirs(): Int = item?.getDragDirs() ?: 0
    fun getItem(): Item1<*> = item!!
    fun getRoot(): View = itemView
}