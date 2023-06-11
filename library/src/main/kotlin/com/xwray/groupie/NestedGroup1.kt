package com.xwray.groupie

import androidx.annotation.CallSuper

/**
 * A base implementation of the Group interface, which supports nesting of Groups to arbitrary depth.
 * You can make a NestedGroup which contains only Items, one which contains Groups, or a mixture.
 * <p>
 * It provides support for notifying the adapter about changes which happen in its child groups.
 */
public abstract class NestedGroup1 : Group1, GroupDataObserver1 {

    private val observable: GroupDataObservable = GroupDataObservable()

    override fun getItemCount(): Int {
        var size = 0
        for (i in 0 until getGroupCount()) {
            val group = getGroup(i)
            size += group.getItemCount()
        }
        return size
    }

    protected fun getItemCountBeforeGroup(group: Group1): Int {
        val groupIndex = getPosition(group)
        return getItemCountBeforeGroup(groupIndex)
    }

    protected fun getItemCountBeforeGroup(groupIndex: Int): Int {
        var size = 0
        for (i in 0 until groupIndex) {
            val currentGroup = getGroup(i)
            size += currentGroup.getItemCount()
        }
        return size
    }

    abstract fun getGroup(position: Int): Group1
    abstract fun getGroupCount(): Int

    override fun getItem(position: Int): Item1<*> {
        var previousPosition = 0
        for (i in 0 until getGroupCount()) {
            val group = getGroup(i)
            val size = group.getItemCount()
            if (size + previousPosition > position) {
                return group.getItem(position - previousPosition)
            }
            previousPosition += size
        }

        throw IndexOutOfBoundsException("Wanted item at $position but there are only ${getItemCount()} items")
    }

    override fun getPosition(item: Item1<*>): Int {
        var previousPosition = 0
        for (i in 0 until getGroupCount()) {
            val group = getGroup(i)
            val position = group.getPosition(item)
            if (position >= 0) {
                return position + previousPosition
            }
            previousPosition += group.getItemCount()
        }
        return -1
    }

    abstract fun getPosition(group: Group1): Int

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver1) {
        observable.registerObserver(groupDataObserver)
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver1) {
        observable.unregisterObserver(groupDataObserver)
    }

    @CallSuper
    open fun add(group: Group1) {
        group.registerGroupDataObserver(this)
    }

    @CallSuper
    open fun <T: Group1> addAll(groups: Collection<T>) {
        for (group in groups) {
            group.registerGroupDataObserver(this)
        }
    }

    @CallSuper
    open fun add(position: Int, group: Group1) {
        group.registerGroupDataObserver(this)
    }

    @CallSuper
    open fun <T: Group1> addAll(position: Int, groups: Collection<T>) {
        for (group in groups) {
            group.registerGroupDataObserver(this)
        }
    }

    @CallSuper
    open fun remove(group: Group1) {
        group.unregisterGroupDataObserver(this)
    }

    @CallSuper
    open fun <T: Group1> removeAll(groups: Collection<T>) {
        for (group in groups) {
            group.unregisterGroupDataObserver(this)
        }
    }

    @CallSuper
    open fun <T: Group1> replaceAll(groups: Collection<T>) {
        val startIndex = getGroupCount() - 1

        for (i in startIndex downTo 0) {
            getGroup(i).unregisterGroupDataObserver(this)
        }

        for (group in groups) {
            group.registerGroupDataObserver(this)
        }
    }

    /**
     * Every item in the group still exists but the data in each has changed (e.g. should rebind).
     */
    @CallSuper
    override fun onChanged(group: Group1) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group), group.getItemCount())
    }

    @CallSuper
    override fun onItemInserted(group: Group1, position: Int) {
        observable.onItemInserted(group, getItemCountBeforeGroup(group) + position)
    }

    @CallSuper
    override fun onItemChanged(group: Group1, position: Int) {
        observable.onItemChanged(group, getItemCountBeforeGroup(group) + position)
    }

    @CallSuper
    override fun onItemChanged(group: Group1, position: Int, payload: Any) {
        observable.onItemChanged(group, getItemCountBeforeGroup(group) + position, payload)
    }

    @CallSuper
    override fun onItemRemoved(group: Group1, position: Int) {
        observable.onItemRemoved(this, getItemCountBeforeGroup(group) + position)
    }

    @CallSuper
    override fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int, payload: Any) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount, payload)
    }

    @CallSuper
    override fun onItemRangeInserted(group: Group1, positionStart: Int, itemCount: Int) {
        observable.onItemRangeInserted(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemRangeRemoved(group: Group1, positionStart: Int, itemCount: Int) {
        observable.onItemRangeRemoved(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemMoved(group: Group1, fromPosition: Int, toPosition: Int) {
        val groupPosition = getItemCountBeforeGroup(group)
        observable.onItemMoved(this, groupPosition + fromPosition, groupPosition + toPosition)
    }

    @CallSuper
    override fun onDataSetInvalidated() {
        observable.onDataSetInvalidated()
    }

    /**
     * A group should use this to notify that there is a change in itself.
     */
    @CallSuper
    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        observable.onItemRangeInserted(this, positionStart, itemCount)
    }

    @CallSuper
    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observable.onItemRangeRemoved(this, positionStart, itemCount)
    }

    @CallSuper
    fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        observable.onItemMoved(this, fromPosition, toPosition)
    }

    @CallSuper
    fun notifyChanged() {
        observable.onChanged(this)
    }

    @CallSuper
    fun notifyItemInserted(position: Int) {
        observable.onItemInserted(this, position)
    }

    @CallSuper
    fun notifyItemChanged(position: Int) {
        observable.onItemChanged(this, position)
    }

    @CallSuper
    fun notifyItemChanged(position: Int, payload: Any) {
        observable.onItemChanged(this, position, payload)
    }

    @CallSuper
    fun notifyItemRemoved(position: Int) {
        observable.onItemRemoved(this, position)
    }

    @CallSuper
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        observable.onItemRangeChanged(this, positionStart, itemCount)
    }

    @CallSuper
    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any) {
        observable.onItemRangeChanged(this, positionStart, itemCount, payload)
    }

    @CallSuper
    fun notifyDataSetInvalidated() {
        observable.onDataSetInvalidated()
    }

    /**
     * Iterate in reverse order in case any observer decides to remove themself from the list
     * in their callback
     */
    private class GroupDataObservable {
        private val observers: MutableList<GroupDataObserver1> = mutableListOf()
        private val observersReversed = observers.asReversed()

        fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int) {
            observersReversed.forEach { it.onItemRangeChanged(group, positionStart, itemCount) }
        }

        fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int, payload: Any) {
            observersReversed.forEach { it.onItemRangeChanged(group, positionStart, itemCount, payload) }
        }

        fun onItemInserted(group: Group1, position: Int) {
            observersReversed.forEach { it.onItemInserted(group, position) }
        }

        fun onItemChanged(group: Group1, position: Int) {
            observersReversed.forEach { it.onItemChanged(group, position) }
        }

        fun onItemChanged(group: Group1, position: Int, payload: Any) {
            observersReversed.forEach { it.onItemChanged(group, position, payload) }
        }

        fun onItemRemoved(group: Group1, position: Int) {
            observersReversed.forEach { it.onItemRemoved(group, position) }
        }

        fun onItemRangeInserted(group: Group1, positionStart: Int, itemCount: Int) {
            observersReversed.forEach { it.onItemRangeInserted(group, positionStart, itemCount) }
        }

        fun onItemRangeRemoved(group: Group1, positionStart: Int, itemCount: Int) {
            observersReversed.forEach { it.onItemRangeRemoved(group, positionStart, itemCount) }
        }

        fun onItemMoved(group: Group1, fromPosition: Int, toPosition: Int) {
            observersReversed.forEach { it.onItemMoved(group, fromPosition, toPosition) }
        }

        fun onChanged(group: Group1) {
            observersReversed.forEach { it.onChanged(group) }
        }

        fun registerObserver(observer: GroupDataObserver1) {
            synchronized(observers) {
                if (observers.contains(observer)) {
                    throw IllegalStateException("Observer $observer was already registered")
                }
                observers.add(observer)
            }
        }

        fun unregisterObserver(observer: GroupDataObserver1) {
            synchronized(observers) {
                observers.remove(observer)
            }
        }

        fun onDataSetInvalidated() {
            observersReversed.forEach { it.onDataSetInvalidated() }
        }
    }
}