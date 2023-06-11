package com.xwray.groupie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope

/**
 * An adapter that holds a list of Groups.
 */
public open class GroupAdapter1<VH : GroupieViewHolder1> : RecyclerView.Adapter<VH>(), GroupDataObserver1 {

    private val groups: MutableList<Group1> = mutableListOf()
    private var onItemClickListener: OnItemClickListener1? = null
    private var onItemLongClickListener: OnItemLongClickListener1? = null
    private var spanCount = 1
    private var lastItemForViewTypeLookup: Item1<VH>? = null

    private val diffUtilCallbacks = object : AsyncDiffUtil1.Callback {
        override fun onDispatchAsyncResult(newGroups: Collection<Group1>) {
            setNewGroups(newGroups)
        }

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
            notifyItemRangeChanged(position, count, payload)
        }
    }

    private val asyncDiffUtil = AsyncDiffUtil1(diffUtilCallbacks)

    private val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return try {
                getItem(position).getSpanSize(spanCount, position)
            } catch (exc: IndexOutOfBoundsException) {
                // TODO: bug in support lib? investigate futher
                spanCount
            }
        }
    }

    fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup = spanSizeLookup

    fun setSpanCount(spanCount: Int) {
        this.spanCount = spanCount
    }

    fun getSpanCount(): Int = spanCount

    /**
     * Updates the adapter with a new list that will be diffed on a background thread
     * and displayed once diff results are calculated.
     * <p>
     * NOTE: This update method is NOT compatible with partial updates (change notifications
     * driven by individual groups and items).  If you update using this method, all partial
     * updates will no longer work and you must use this method to update exclusively.
     * <br/> <br/>
     * If you want to receive a callback once the update is complete call the
     * {@link #updateAsync(List, boolean, OnAsyncUpdateListener)} version
     * <p>
     * This will default detectMoves to true.
     *
     * @param newGroups List of {@link Group}
     */
    @SuppressWarnings("unused")
    fun updateAsync(scope: CoroutineScope, newGroups: List<Group1>) {
        updateAsync(scope, newGroups, true, null)
    }

    /**
     * Updates the adapter with a new list that will be diffed on a background thread
     * and displayed once diff results are calculated.
     * <p>
     * NOTE: This update method is NOT compatible with partial updates (change notifications
     * driven by individual groups and items).  If you update using this method, all partial
     * updates will no longer work and you must use this method to update exclusively.
     * <br/> <br/>
     *
     * This will default detectMoves to true.
     *
     * @see #updateAsync(List, boolean, OnAsyncUpdateListener)
     * @param newGroups List of {@link Group}
     */
    @SuppressWarnings("unused")
    fun updateAsync(scope: CoroutineScope, newGroups: List<Group1>, onAsyncUpdateListener: OnAsyncUpdateListener1?) {
        updateAsync(scope, newGroups, true, onAsyncUpdateListener)
    }

    /**
     * Updates the adapter with a new list that will be diffed on a background thread
     * and displayed once diff results are calculated.
     * <p>
     * NOTE: This update method is NOT compatible with partial updates (change notifications
     * driven by individual groups and items).  If you update using this method, all partial
     * updates will no longer work and you must use this method to update exclusively.
     *
     * @param newGroups List of {@link Group}
     * @param onAsyncUpdateListener Optional callback for when the async update is complete
     * @param detectMoves Boolean is passed to {@link DiffUtil#calculateDiff(DiffUtil.Callback, boolean)}. Set to true
     *                    if you want DiffUtil to detect moved items.
     */
    @SuppressWarnings("unused")
    fun updateAsync(scope: CoroutineScope, newGroups: List<Group1>, detectMoves: Boolean, onAsyncUpdateListener: OnAsyncUpdateListener1?) {
        // Fast simple first insert
        if (groups.isEmpty()) {
            update(newGroups, detectMoves)
            onAsyncUpdateListener?.onUpdateComplete()
            return
        }
        val oldGroups = groups.toList()
        val diffUtilCallback = DiffCallback1(oldGroups, newGroups)
        asyncDiffUtil.calculateDiff(scope, newGroups, diffUtilCallback, onAsyncUpdateListener, detectMoves)
    }

    /**
     * Replaces the groups within the adapter without using DiffUtil, and therefore without animations.
     *
     *
     * For animation support, use [GroupAdapter.update] or [GroupAdapter.updateAsync] instead.
     *
     * @param newGroups List of [Group]
     */
    fun replaceAll(newGroups: Collection<Group1>) {
        setNewGroups(newGroups)
        notifyDataSetChanged()
    }

    /**
     * Updates the adapter with a new list that will be diffed on the *main* thread
     * and displayed once diff results are calculated. Not recommended for huge lists.
     *
     *
     * This will default detectMoves to true.
     *
     * @param newGroups List of [Group]
     */
    @SuppressWarnings("unused")
    fun update(newGroups: Collection<Group1>) {
        update(newGroups, true)
    }

    /**
     * Updates the adapter with a new list that will be diffed on the *main* thread
     * and displayed once diff results are calculated. Not recommended for huge lists.
     * @param newGroups List of [Group]
     * @param detectMoves is passed to [DiffUtil.calculateDiff]. Set to false
     * if you don't want DiffUtil to detect moved items.
     */
    fun update(newGroups: Collection<Group1>, detectMoves: Boolean) {
        val oldGroups = groups.toList()
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback1(oldGroups, newGroups),
            detectMoves
        )
        setNewGroups(newGroups)
        diffResult.dispatchUpdatesTo(diffUtilCallbacks)
    }

    /**
     * Optionally register an [OnItemClickListener] that listens to click at the root of
     * each Item where [Item.isClickable] returns true
     *
     * @param onItemClickListener The click listener to set
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener1?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * Optionally register an [OnItemLongClickListener] that listens to long click at the root of
     * each Item where [Item.isLongClickable] returns true
     *
     * @param onItemLongClickListener The long click listener to set
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener1?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val item = getItemForViewType(viewType)
        val itemView = inflater.inflate(item.getLayout(), parent, false)
        return item.createViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Never called (all binds go through the version with payload)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        val contentItem = getItem(position)
        contentItem.bind(holder, position, payloads, onItemClickListener, onItemLongClickListener)
    }

    override fun onViewRecycled(holder: VH) {
        val contentItem = holder.getItem() as Item1<VH>
        contentItem.unbind(holder)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean {
        val contentItem = holder.getItem()
        return contentItem.isRecyclable()
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val item = getItem(holder)
        //noinspection unchecked
        item.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        val item = getItem(holder)
        //noinspection unchecked
        item.onViewDetachedFromWindow(holder)
    }

    override fun getItemViewType(position: Int): Int {
        lastItemForViewTypeLookup = getItem(position)
        return lastItemForViewTypeLookup!!.getViewType()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).getId()
    }

    fun getItem(holder: VH): Item1<VH> {
        //noinspection unchecked
        return holder.getItem() as Item1<VH>
    }

    fun getItem(position: Int): Item1<VH> = GroupUtils1.getItem(groups, position) as Item1<VH>

    fun getAdapterPosition(contentItem: Item1<VH>): Int {
        var count = 0
        for (group in groups) {
            val index = group.getPosition(contentItem)
            if (index >= 0) return index + count
            count += group.getItemCount()
        }
        return -1
    }

    /**
     * The position in the flat list of individual items at which the group starts
     */
    fun getAdapterPosition(group: Group1): Int {
        val index = groups.indexOf(group)
        if (index == -1) return -1
        var position = 0
        for (i in 0 until index) {
            position += groups[i].getItemCount()
        }
        return position
    }

    /**
     * Returns the number of top-level groups present in the adapter.
     */
    fun getGroupCount(): Int = groups.size

    /**
     * This returns the total number of items contained in all groups in this adapter
     */
    override fun getItemCount(): Int = GroupUtils1.getItemCount(groups)

    /**
     * This returns the total number of items contained in the top level group at the passed index
     */
    fun getItemCountForGroup(groupIndex: Int): Int {
        if (groupIndex >= groups.size) {
            throw IndexOutOfBoundsException("Requested group index $groupIndex but there are ${groups.size} groups")
        }
        return groups[groupIndex].getItemCount()
    }

    /**
     * This returns the total number of items contained in the top level group at the passed index
     * @deprecated This method has been deprecated in favour of {@link #getItemCountForGroup(int)}. Please use that method instead.
     */
    @Deprecated("", ReplaceWith(""))
    fun getItemCount(groupIndex: Int): Int = getItemCountForGroup(groupIndex)

    fun clear() {
        for (group in groups) {
            group.unregisterGroupDataObserver(this)
        }
        groups.clear()
        notifyDataSetChanged()
    }

    fun add(group: Group1) {
        val itemCountBeforeGroup = itemCount
        group.registerGroupDataObserver(this)
        groups.add(group)
        notifyItemRangeInserted(itemCountBeforeGroup, group.getItemCount())
    }

    /**
     * Adds the contents of the list of groups, in order, to the end of the adapter contents.
     * All groups in the list must be non-null.
     */
    fun addAll(groups: Collection<Group1>) {
        val itemCountBeforeGroup = itemCount
        var additionalSize = 0
        for (group in groups) {
            additionalSize += group.getItemCount()
            group.registerGroupDataObserver(this)
        }
        this.groups.addAll(groups)
        notifyItemRangeInserted(itemCountBeforeGroup, additionalSize)
    }

    fun remove(group: Group1) {
        val position = groups.indexOf(group)
        remove(position, group)
    }

    fun removeAll(groups: Collection<Group1>) {
        for (group in groups) {
            remove(group)
        }
    }

    /**
     * Remove a Group at a raw adapter position
     * @param position raw adapter position of Group to remove
     */
    fun removeGroupAtAdapterPosition(position: Int) {
        val group = getGroupAtAdapterPosition(position)
        remove(position, group)
    }

    /**
     * Remove a Group at a raw adapter position.
     * @param adapterPosition raw adapter position of Group to remove.
     * @deprecated This method has been deprecated in favor of {@link #removeGroupAtAdapterPosition(int)}. Please use that method instead.
     */
    @Deprecated("", ReplaceWith("removeGroupAtAdapterPosition"))
    fun removeGroup(adapterPosition: Int) = removeGroupAtAdapterPosition(adapterPosition)

    fun remove(position: Int, group: Group1) {
        val itemCountBeforeGroup = getItemCountBeforeGroup(position)
        group.unregisterGroupDataObserver(this)
        groups.removeAt(position)
        notifyItemRangeRemoved(itemCountBeforeGroup, group.getItemCount())
    }

    fun add(index: Int, group: Group1) {
        group.registerGroupDataObserver(this)
        groups.add(index, group)
        val itemCountBeforeGroup: Int = getItemCountBeforeGroup(index)
        notifyItemRangeInserted(itemCountBeforeGroup, group.getItemCount())
    }

    /**
     * Get group, given a top level group position. If you want to get a group at an adapter position
     * then use {@link #getGroupAtAdapterPosition(int)}
     *
     * @param position Top level group position
     * @return Group at that position or throws {@link IndexOutOfBoundsException}
     */
    @SuppressWarnings("WeakerAccess")
    fun getTopLevelGroup(position: Int): Group1 = groups[position]

    /**
     * Get group, given a raw adapter position. If you want to get a top level group by position
     * then use {@link #getTopLevelGroup(int)}
     *
     * @param position raw adapter position
     * @return Group at that position or throws {@link IndexOutOfBoundsException}
     */
    fun getGroupAtAdapterPosition(position: Int): Group1 {
        var previous = 0
        var size = 0
        for (group in groups) {
            size = group.getItemCount()
            if (position - previous < size) return group
            previous += group.getItemCount()
        }
        throw IndexOutOfBoundsException("Requested position $position in group adapter but there are only $previous items")
    }

    /**
     * Get group, given a raw adapter position. If you want to get a top level group by position
     * then use {@link #getTopLevelGroup(int)}
     *
     * @param adapterPosition raw adapter position
     * @return Group at that position or throws {@link IndexOutOfBoundsException}
     * @deprecated This method is deprecated and has been replaced with {@link #getGroupAtAdapterPosition(int)}. Please use that method instead.
     */
    @Deprecated("", ReplaceWith("getGroupAtAdapterPosition"))
    fun getGroup(adapterPosition: Int): Group1 = getGroupAtAdapterPosition(adapterPosition)

    /**
     * Returns the Group which contains this item or throws an {@link IndexOutOfBoundsException} if not present.
     * This is the item's <b>direct</b> parent, not necessarily one of the top level groups present in this adapter.
     * @param contentItem Item to find the parent group for.
     * @return Parent group of this item.
     */
    fun getGroup(contentItem: Item1<VH>): Group1 {
        for (group in groups) {
            if (group.getPosition(contentItem) >= 0) return group
        }
        throw IndexOutOfBoundsException("Item is not present in adapter or in any group")
    }

    override fun onChanged(group: Group1) {
        notifyItemRangeChanged(getAdapterPosition(group), group.getItemCount())
    }

    override fun onItemInserted(group: Group1, position: Int) {
        notifyItemInserted(getAdapterPosition(group) + position)
    }

    override fun onItemChanged(group: Group1, position: Int) {
        notifyItemChanged(getAdapterPosition(group) + position)
    }

    override fun onItemChanged(group: Group1, position: Int, payload: Any) {
        notifyItemChanged(getAdapterPosition(group) + position, payload)
    }

    override fun onItemRemoved(group: Group1, position: Int) {
        notifyItemRemoved(getAdapterPosition(group) + position)
    }

    override fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemRangeChanged(group: Group1, positionStart: Int, itemCount: Int, payload: Any) {
        notifyItemRangeChanged(getAdapterPosition(group) + positionStart, itemCount, payload)
    }

    override fun onItemRangeInserted(group: Group1, positionStart: Int, itemCount: Int) {
        notifyItemRangeInserted(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemRangeRemoved(group: Group1, positionStart: Int, itemCount: Int) {
        notifyItemRangeRemoved(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemMoved(group: Group1, fromPosition: Int, toPosition: Int) {
        val groupAdapterPosition = getAdapterPosition(group)
        notifyItemMoved(groupAdapterPosition + fromPosition, groupAdapterPosition + toPosition)
    }

    override fun onDataSetInvalidated() = notifyDataSetChanged()

    /**
     * This idea was copied from Epoxy. :wave: Bright idea guys!
     * <p>
     * Find the model that has the given view type so we can create a viewholder for that model.
     * <p>
     * To make this efficient, we rely on the RecyclerView implementation detail that {@link
     * GroupAdapter#getItemViewType(int)} is called immediately before {@link
     * GroupAdapter#onCreateViewHolder(android.view.ViewGroup, int)}. We cache the last model
     * that had its view type looked up, and unless that implementation changes we expect to have a
     * very fast lookup for the correct model.
     * <p>
     * To be safe, we fallback to searching through all models for a view type match. This is slow and
     * shouldn't be needed, but is a guard against RecyclerView behavior changing.
     */
    private fun getItemForViewType(viewType: Int): Item1<VH> {
        if (lastItemForViewTypeLookup != null && lastItemForViewTypeLookup?.getViewType() == viewType) {
            // We expect this to be a hit 100% of the time
            return lastItemForViewTypeLookup!!
        }

        // To be extra safe in case RecyclerView implementation details change...
        for (i in 0 until itemCount) {
            val item = getItem(i)
            if (item.getViewType() == viewType) return item
        }

        throw IllegalStateException("Could not find model for view type $viewType")
    }

    private fun getItemCountBeforeGroup(groupIndex: Int): Int {
        var count = 0
        for (group in groups.subList(0, groupIndex)) {
            count += group.getItemCount()
        }
        return count
    }

    private fun setNewGroups(newGroups: Collection<Group1>) {
        for (group in groups) {
            group.unregisterGroupDataObserver(this)
        }

        groups.clear()
        groups.addAll(newGroups)

        for (group in newGroups) {
            group.registerGroupDataObserver(this)
        }
    }
}