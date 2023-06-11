package com.xwray.groupie

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

/**
 * A group which has a list of contents and an optional header and footer.
 */
public open class Section1<T: Group1> : NestedGroup1 {
    private var header: Group1? = null
    private var footer: Group1? = null
    private var placeholder: Group1? = null
    private val children: MutableList<Group1> = mutableListOf()
    private var hideWhenEmpty = false
    private var isHeaderAndFooterVisible = true
    private var isPlaceholderVisible = false

    constructor(): this(null, emptyList())

    constructor(header: Group1?): this(header, emptyList())

    constructor(children: Collection<T>): this(null, children)

    constructor(header: Group1?, children: Collection<T>) {
        this.header = header
        header?.registerGroupDataObserver(this)
        addAll(children)
    }

    override fun add(position: Int, group: Group1) {
        super.add(position, group)
        children.add(position, group)
        val notifyPosition = getHeaderItemCount() + GroupUtils1.getItemCount(children.subList(0, position))
        notifyItemRangeInserted(notifyPosition, group.getItemCount())
        refreshEmptyState()
    }

    override fun <T : Group1> addAll(groups: Collection<T>) {
        if (groups.isEmpty()) return
        super.addAll(groups)
        val position = getItemCountWithoutFooter()
        children.addAll(groups)
        notifyItemRangeInserted(position, GroupUtils1.getItemCount(groups))
        refreshEmptyState()
    }

    override fun <T : Group1> addAll(position: Int, groups: Collection<T>) {
        if (groups.isEmpty()) return

        super.addAll(position, groups)
        children.addAll(position, groups)

        val notifyPosition = getHeaderItemCount() + GroupUtils1.getItemCount(children.subList(0, position))
        notifyItemRangeInserted(notifyPosition, GroupUtils1.getItemCount(groups))
        refreshEmptyState()
    }

    override fun add(group: Group1) {
        super.add(group)
        val position = getItemCountWithoutFooter()
        children.add(group)
        notifyItemRangeInserted(position, group.getItemCount())
        refreshEmptyState()
    }

    override fun remove(group: Group1) {
        super.remove(group)
        val position = getItemCountBeforeGroup(group)
        children.remove(group)
        notifyItemRangeRemoved(position, group.getItemCount())
        refreshEmptyState()
    }

    override fun <T : Group1> removeAll(groups: Collection<T>) {
        if (groups.isEmpty()) return

        super.removeAll(groups)
        for (group in groups) {
            val position = getItemCountBeforeGroup(group)
            children.remove(group)
            notifyItemRangeRemoved(position, group.getItemCount())
        }
        refreshEmptyState()
    }

    override fun <T : Group1> replaceAll(groups: Collection<T>) {
        if (groups.isEmpty()) return

        super.replaceAll(groups)
        children.clear()
        children.addAll(groups)

        notifyDataSetInvalidated()
        refreshEmptyState()
    }

    /**
     * Get the list of all groups in this section, wrapped in a new [ArrayList]. This
     * does **not include headers, footers or placeholders**.
     * @return The list of all groups in this section, wrapped in a new [ArrayList]
     */
    fun getGroups(): List<Group1> = children.toList()

    /**
     * Remove all existing body content.
     */
    fun clear() {
        if (children.isEmpty()) return
        removeAll(children.toList())
    }

    /**
     * Replace all existing body content and dispatch fine-grained change notifications to the
     * parent using DiffUtil.
     *
     *
     * Item comparisons are made using:
     * - Item.isSameAs(Item otherItem) (are items the same?)
     * - Item.hasSameContentAs() (are contents the same?)
     *
     *
     * If you don't customize getId() or isSameAs() and hasSameContentAs(), the default implementations will return false,
     * meaning your Group will consider every update a complete change of everything.
     *
     *
     * This will default detectMoves to true.
     *
     * @see .update
     * @param newBodyGroups The new content of the section
     */
    fun <T: Group1> update(newBodyGroups: Collection<T>) {
        update(newBodyGroups, true)
    }

    /**
     * Replace all existing body content and dispatch fine-grained change notifications to the
     * parent using DiffUtil.
     * <p>
     * Item comparisons are made using:
     * - Item.isSameAs(Item otherItem) (are items the same?)
     * - Item.hasSameContentAs() (are contents the same?)
     * <p>
     * If you don't customize getId() or isSameAs() and hasSameContentAs(), the default implementations will return false,
     * meaning your Group will consider every update a complete change of everything.
     *
     * @param newBodyGroups The new content of the section
     * @param detectMoves is passed to {@link DiffUtil#calculateDiff(DiffUtil.Callback, boolean)}. Set to false if you
     *                    don't want DiffUtil to detect moved items.
     */
    fun <T: Group1> update(newBodyGroups: Collection<T>, detectMoves: Boolean) {
        val oldBodyGroups = children.toList()
        val diffResult = DiffUtil.calculateDiff(DiffCallback1(oldBodyGroups, newBodyGroups), detectMoves)
        update(newBodyGroups, diffResult)
    }

    /**
     * Overloaded version of update method in which you can pass your own DiffUtil.DiffResult
     * @param newBodyGroups The new content of the section
     */
    fun update(newBodyGroups: Collection<Group1>, diffResult: DiffUtil.DiffResult) {
        super.removeAll(children)
        children.clear()
        children.addAll(newBodyGroups)
        super.addAll(newBodyGroups)

        diffResult.dispatchUpdatesTo(listUpdateCallback)
        refreshEmptyState()
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(getHeaderItemCount() + position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(getHeaderItemCount() + position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val headerItemCount = getHeaderItemCount()
            notifyItemMoved(headerItemCount + fromPosition, headerItemCount + toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            notifyItemRangeChanged(getHeaderItemCount() + position, count, payload ?: return)
        }
    }

    /**
     * Optional. Set a placeholder for when the section's body is empty.
     * <p>
     * If setHideWhenEmpty(true) is set, then the empty placeholder will not be shown.
     *
     * @param placeholder A placeholder to be shown when there is no body content
     */
    fun setPlaceholder(placeholder: Group1) {
        removePlaceholder()
        this.placeholder = placeholder
        refreshEmptyState()
    }

    fun removePlaceholder() {
        hidePlaceholder()
        placeholder = null
    }

    private fun showPlaceholder() {
        if (isPlaceholderVisible || placeholder == null) return
        isPlaceholderVisible = true
        notifyItemRangeInserted(getHeaderItemCount(), placeholder!!.getItemCount())
    }

    private fun hidePlaceholder() {
        if (!isPlaceholderVisible || placeholder == null) return
        isPlaceholderVisible = false
        notifyItemRangeRemoved(getHeaderItemCount(), placeholder!!.getItemCount())
    }

    /**
     * Whether a section's contents are visually empty
     */
    protected fun isEmpty(): Boolean = children.isEmpty() || GroupUtils1.getItemCount(children) == 0

    private fun hideDecorations() {
        if (!isHeaderAndFooterVisible && !isPlaceholderVisible) return
        val count = getHeaderItemCount() + getPlaceholderItemCount() + getFooterItemCount()
        isHeaderAndFooterVisible = false
        isPlaceholderVisible = false
        notifyItemRangeRemoved(0, count)
    }

    protected fun refreshEmptyState() {
        if (isEmpty()) {
            if (hideWhenEmpty) {
                hideDecorations()
            } else {
                showPlaceholder()
                showHeadersAndFooters()
            }
        } else {
            hidePlaceholder()
            showHeadersAndFooters()
        }
    }

    private fun showHeadersAndFooters() {
        if (isHeaderAndFooterVisible) return
        isHeaderAndFooterVisible = true
        notifyItemRangeInserted(0, getHeaderItemCount())
        notifyItemRangeInserted(getItemCountWithoutFooter(), getFooterItemCount())
    }

    private fun getBodyItemCount() = if (isPlaceholderVisible) getPlaceholderItemCount() else GroupUtils1.getItemCount(children)

    private fun getItemCountWithoutFooter() = getBodyItemCount() + getHeaderItemCount()

    private fun getHeaderCount(): Int = if (header == null || !isHeaderAndFooterVisible) 0 else 1

    private fun getHeaderItemCount(): Int = if (getHeaderCount() == 0) 0 else (header?.getItemCount() ?: 0)

    private fun getFooterItemCount(): Int = if (getFooterCount() == 0) 0 else (footer?.getItemCount() ?: 0)

    private fun getFooterCount(): Int = if (footer == null || !isHeaderAndFooterVisible) 0 else 1

    private fun getPlaceholderCount(): Int = if (isPlaceholderVisible) 1 else 0

    override fun getGroup(position: Int): Group1 {
        var positionLocal = position
        if (isHeaderShown() && positionLocal == 0) return header!!
        positionLocal -= getHeaderCount()
        if (isPlaceholderShown() && positionLocal == 0) return placeholder!!
        positionLocal -= getPlaceholderCount()
        return if (positionLocal == children.size) {
            if (isFooterShown()) {
                footer!!
            } else {
                throw IndexOutOfBoundsException("Wanted group at position $positionLocal but there are only ${getGroupCount()} groups")
            }
        } else {
            children[positionLocal]
        }
    }

    override fun getGroupCount(): Int {
        return getHeaderCount() + getFooterCount() + getPlaceholderCount() + children.size
    }

    override fun getPosition(group: Group1): Int {
        var count = 0
        if (isHeaderShown()) {
            if (group == header) return 0
        }
        count += getHeaderCount()
        if (isPlaceholderShown()) {
            if (group == placeholder) return count
        }
        count += getPlaceholderCount()

        val index = children.indexOf(group)
        if (index >= 0) return count + index
        count += children.size

        if (isFooterShown() && footer == group) return count
        return -1
    }

    private fun isHeaderShown() = getHeaderCount() > 0

    private fun isFooterShown() = getFooterCount() > 0

    private fun isPlaceholderShown() = getPlaceholderCount() > 0

    fun setHeader(header: Group1) {
        header.unregisterGroupDataObserver(this)
        val previousHeaderItemCount = getHeaderItemCount()
        this.header = header
        header.registerGroupDataObserver(this)
        notifyHeaderItemsChanged(previousHeaderItemCount)
    }

    fun removeHeader() {
        header?.unregisterGroupDataObserver(this) ?: return
        val previousHeaderItemCount = getHeaderItemCount()
        header = null
        notifyHeaderItemsChanged(previousHeaderItemCount)
    }

    private fun notifyHeaderItemsChanged(previousHeaderItemCount: Int) {
        val newHeaderItemCount = getHeaderItemCount()
        if (previousHeaderItemCount > 0) {
            notifyItemRangeRemoved(0, previousHeaderItemCount)
        }
        if (newHeaderItemCount > 0) {
            notifyItemRangeInserted(0, newHeaderItemCount)
        }
    }

    fun setFooter(footer: Group1) {
        this.footer?.unregisterGroupDataObserver(this)
        val previousFooterItemCount = getFooterItemCount()
        this.footer = footer
        footer.registerGroupDataObserver(this)
        notifyFooterItemsChanged(previousFooterItemCount)
    }

    fun removeFooter() {
        footer?.unregisterGroupDataObserver(this) ?: return
        val previousFooterItemCount = getFooterItemCount()
        footer = null
        notifyFooterItemsChanged(previousFooterItemCount)
    }

   private fun notifyFooterItemsChanged(previousFooterItemCount: Int) {
        val newFooterItemCount = getFooterItemCount()
        if (previousFooterItemCount > 0) {
            notifyItemRangeRemoved(getItemCountWithoutFooter(), previousFooterItemCount)
        }
        if (newFooterItemCount > 0) {
            notifyItemRangeInserted(getItemCountWithoutFooter(), newFooterItemCount)
        }
    }

    fun setHideWhenEmpty(hide: Boolean) {
        if (hideWhenEmpty == hide) return
        hideWhenEmpty = hide
        refreshEmptyState()
    }

    override fun onItemInserted(group: Group1, position: Int) {
        super.onItemInserted(group, position)
        refreshEmptyState()
    }

    override fun onItemRemoved(group: Group1, position: Int) {
        super.onItemRemoved(group, position)
        refreshEmptyState()
    }

    override fun onItemRangeInserted(group: Group1, positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(group, positionStart, itemCount)
        refreshEmptyState()
    }

    override fun onItemRangeRemoved(group: Group1, positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(group, positionStart, itemCount)
        refreshEmptyState()
    }

    private fun getPlaceholderItemCount(): Int {
        if (isPlaceholderVisible && placeholder != null) {
            return placeholder!!.getItemCount()
        }
        return 0
    }
}