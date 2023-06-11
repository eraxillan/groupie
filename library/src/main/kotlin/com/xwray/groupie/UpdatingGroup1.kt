package com.xwray.groupie

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
/*
@Deprecated("")
public class UpdatingGroup1 : NestedGroup1 {
    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            notifyItemRangeChanged(position, count)
        }
    }

    private val items: MutableList<Item<*>> = mutableListOf()

    fun update(newItems: List<Item<*>>) {
        val diffResult = DiffUtil.calculateDiff(UpdatingCallback(newItems))
        super.removeAll(items)
        items.clear()
        super.addAll(newItems)
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(listUpdateCallback)
    }

    fun getGroup(position: Int): Group1 = items[position]

    fun getGroupCount(): Int = items.size

    fun getPosition(group: Group1): Int {
        return if (group is Item1<*>) {
            items.indexOf(group)
        } else {
            -1
        }
    }

    private inner class UpdatingCallback(val newItems: List<Item1<*>>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = items.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = items[oldItemPosition]
            val newItem = newItems[newItemPosition]
            if (oldItem.viewType != newItem.viewType) {
                return false
            }
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = items[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.hasSameContentAs(newItem)
        }
    }
}*/