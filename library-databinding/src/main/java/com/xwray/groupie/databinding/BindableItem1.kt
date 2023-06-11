package com.xwray.groupie.databinding

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.Item1
import com.xwray.groupie.OnItemClickListener1
import com.xwray.groupie.OnItemLongClickListener1

/**
 * The base unit of content for a GroupAdapter.
 * <p>
 * Because an Item is a Group of size one, you don't need to use Groups directly if you don't want;
 * simply mix and match Items and add directly to the adapter.
 * <p>
 * If you want to use Groups, because Item extends Group, you can mix and match adding Items and
 * other Groups directly to the adapter.
 *
 * @param <T> The ViewDataBinding subclass associated with this Item.
 */
public abstract class BindableItem1<T: ViewDataBinding> protected constructor(id: Long) : Item1<GroupieViewHolder1<T>>(id) {

    override fun createViewHolder(itemView: View): GroupieViewHolder1<T> {
        val viewDataBinding = DataBindingUtil.bind<T>(itemView) ?: throw NullPointerException("Data binding failed")
        return GroupieViewHolder1(viewDataBinding)
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewHolder              The viewHolder to bind
     * @param position                The adapter position
     * @param payloads                Any payloads (this list may be empty)
     * @param onItemClickListener     An optional adapter-level click listener
     * @param onItemLongClickListener An optional adapter-level long click listener
     */
    override fun bind(
        viewHolder: GroupieViewHolder1<T>,
        position: Int,
        payloads: List<Any>,
        onItemClickListener: OnItemClickListener1?,
        onItemLongClickListener: OnItemLongClickListener1?
    ) {
        super.bind(viewHolder, position, payloads, onItemClickListener, onItemLongClickListener)
        viewHolder.binding.executePendingBindings()
    }

    override fun bind(viewHolder: GroupieViewHolder1<T>, position: Int) {
        throw RuntimeException("Doesn't get called")
    }

    override fun bind(viewHolder: GroupieViewHolder1<T>, position: Int, payloads: List<Any>) {
        bind(viewHolder.binding, position, payloads)
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewBinding The ViewDataBinding to bind
     * @param position    The adapter position
     */
    abstract fun bind(viewBinding: ViewBinding, position: Int)

    /**
     * Perform any actions required to set up the view for display.
     * <p>
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param viewBinding The ViewDataBinding to bind
     * @param position    The adapter position
     * @param payloads    A list of payloads (may be empty)
     */
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    fun bind(viewBinding: ViewBinding, position: Int, payloads: List<Any>) = bind(viewBinding, position)
}