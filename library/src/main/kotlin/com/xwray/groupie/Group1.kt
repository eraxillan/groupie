package com.xwray.groupie

/**
 * A group of items, to be used in an adapter.
 */
public interface Group1 {

    fun getItemCount(): Int
    fun getItem(position: Int): Item1<*>
    fun getPosition(item: Item1<*>): Int
    fun registerGroupDataObserver(groupDataObserver: GroupDataObserver1)
    fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver1)
}