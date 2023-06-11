package com.xwray.groupie

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicLong

public abstract class Item1<VH: GroupieViewHolder1> : Group1, SpanSizeProvider1 {

    protected var parentDataObserver: GroupDataObserver1? = null
    private val id: Long
    private val extras: Map<String, Any> = mutableMapOf()

    constructor(): this(ID_COUNTER.decrementAndGet())

    constructor(id: Long) {
        this.id = id
    }

    open fun createViewHolder(itemView: View): VH {
        return GroupieViewHolder(itemView) as VH
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewHolder          The viewHolder to bind
     * @param position            The adapter position
     * @param payloads            Any payloads (this list may be empty)
     * @param onItemClickListener An optional adapter-level click listener
     * @param onItemLongClickListener An optional adapter-level long click listener
     */
    @CallSuper
    open fun bind(
        viewHolder: VH,
        position: Int,
        payloads: List<Any>,
        onItemClickListener: OnItemClickListener1?,
        onItemLongClickListener: OnItemLongClickListener1?
    ) {
        viewHolder.bind(this, onItemClickListener, onItemLongClickListener)
        bind(viewHolder, position, payloads)
    }

    abstract fun bind(viewHolder: VH, position: Int)

    /**
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param viewHolder The ViewHolder to bind
     * @param position The adapter position
     * @param payloads A list of payloads (may be empty)
     */
    open fun bind(viewHolder: VH, position: Int, payloads: List<Any>) = bind(viewHolder, position)

    /**
     * Do any cleanup required for the viewholder to be reused.
     *
     * @param viewHolder The ViewHolder being recycled
     */
    @CallSuper
    fun unbind(viewHolder: VH) = viewHolder.unbind()

    /**
     * Whether the view should be recycled. Return false to prevent the view from being recycled.
     * (Note that it may still be re-bound.)
     *
     * @return Whether the view should be recycled.
     * @see RecyclerView.Adapter#onFailedToRecycleView(RecyclerView.ViewHolder)
     */
    fun isRecyclable(): Boolean = true

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount

    fun getSwipeDirs(): Int = 0
    fun getDragDirs(): Int = 0

    @LayoutRes
    abstract fun getLayout(): Int

    fun onViewAttachedToWindow(viewHolder: VH) {}
    fun onViewDetachedFromWindow(viewHolder: VH) {}

    /**
     * Override this method if the same layout needs to have different viewTypes.
     * @return the viewType, defaults to the layoutId
     * @see RecyclerView.Adapter#getItemViewType(int)
     */
    fun getViewType(): Int = getLayout()

    override fun getItemCount(): Int = 1

    override fun getItem(position: Int): Item1<*> {
        if (position == 0) {
            return this
        } else {
            throw IndexOutOfBoundsException("Wanted item at position $position but an Item is a Group of size 1")
        }
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver1) {
        parentDataObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver1) {
        parentDataObserver = null
    }

    override fun getPosition(item: Item1<*>): Int = if (this == item) 0 else -1

    fun isClickable(): Boolean = true
    fun isLongClickable(): Boolean = true

    fun notifyChanged() {
        parentDataObserver?.onItemChanged(this, 0)
    }

    fun notifyChanged(payload: Any) {
        parentDataObserver?.onItemChanged(this, 0, payload)
    }

    /**
     * A set of key/value pairs stored on the ViewHolder that can be useful for distinguishing
     * items of the same view type.
     *
     * @return The map of extras
     */
    fun getExtras(): Map<String, Any> = extras

    /**
     * If you don't specify an id, this id is an auto-generated unique negative integer for each Item (the less
     * likely to conflict with your model IDs.)
     * <p>
     * You may prefer to override it with the ID of a model object, for example the primary key of
     * an object from a database that it represents.
     *
     * @return A unique id
     */
    fun getId(): Long = id

    /**
     * Whether two item objects represent the same underlying data when compared using DiffUtil,
     * even if there has been a change in that data.
     * <p>
     * The default implementation compares both view type and id.
     *
     * @return True if the items are the same, false otherwise.
     */
    fun isSameAs(other: Item1<*>): Boolean {
        if (getViewType() != other.getViewType()) {
            return false
        }
        return getId() == other.getId()
    }

    /**
     * Whether this item has the same content as another when compared using DiffUtil.
     * <p>
     * After two items have been determined to be the same using {@link #isSameAs(Item)} this function
     * should check whether their contents are the same.
     * <p>
     * The default implementation does this using {@link #equals(Object)}
     *
     * @return True if both items have the same content, false otherwise
     */
    fun hasSameContentAs(other: Item1<*>): Boolean = this == other

    fun getChangePayload(newItem: Item1<*>): Any? = null

    private companion object {
        val ID_COUNTER = AtomicLong(0)
    }
}