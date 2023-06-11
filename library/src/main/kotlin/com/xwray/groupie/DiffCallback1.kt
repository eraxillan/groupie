package com.xwray.groupie

import androidx.recyclerview.widget.DiffUtil

class DiffCallback1<T: Group1>(oldGroups: Collection<T>, newGroups: Collection<T>) : DiffUtil.Callback() {

    private val oldListSize: Int
    private val newListSize: Int
    private val oldGroupsLocal: Collection<T>
    private val newGroupsLocal: Collection<T>

    init {
        oldListSize = GroupUtils1.getItemCount(oldGroups)
        newListSize = GroupUtils1.getItemCount(newGroups)
        oldGroupsLocal = oldGroups
        newGroupsLocal = newGroups
    }

    override fun getOldListSize(): Int = oldListSize

    override fun getNewListSize(): Int = newListSize

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = GroupUtils1.getItem(oldGroupsLocal, oldItemPosition)
        val newItem = GroupUtils1.getItem(newGroupsLocal, newItemPosition)
        return newItem.isSameAs(oldItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = GroupUtils1.getItem(oldGroupsLocal, oldItemPosition)
        val newItem = GroupUtils1.getItem(newGroupsLocal, newItemPosition)
        return newItem.hasSameContentAs(oldItem)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = GroupUtils1.getItem(oldGroupsLocal, oldItemPosition)
        val newItem = GroupUtils1.getItem(newGroupsLocal, newItemPosition)
        return oldItem.getChangePayload(newItem)
    }
}