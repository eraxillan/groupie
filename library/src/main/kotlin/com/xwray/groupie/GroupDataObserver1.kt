package com.xwray.groupie

public interface GroupDataObserver1 {
    fun onChanged(group: Group1)
    fun onItemInserted(group: Group1, position: Int)
    fun onItemChanged(group: Group1, position: Int)
    fun onItemChanged(group: Group1, position: Int, payload: Any)
    fun onItemRemoved(group: Group1, position: Int)
    fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int)
    fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int, payload: Any)
    fun onItemRangeInserted(group: Group1, positionStart: Int, itemCount: Int)
    fun onItemRangeRemoved(group: Group1, positionStart: Int, itemCount: Int)
    fun onItemMoved(group: Group1, fromPosition: Int, toPosition: Int)
    fun onDataSetInvalidated()
}