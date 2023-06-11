package com.xwray.groupie

/**
 * An ExpandableGroup is one "base" content item with a list of children (any of which
 * may themselves be a group.)
 **/
/*public class ExpandableGroup1<T: ExpandableItem>: NestedGroup {

    private var isExpandedLocal: Boolean = false
    private val parent: Any
    private val children: List<Group> = emptyList()

    constructor(expandableItem: ExpandableItem) {
        parent = expandableItem
        expandableItem.setExpandableGroup(this)
    }

    constructor(expandableItem: T, isExpanded: Boolean) {
        parent = expandableItem
        expandableItem.setExpandableGroup(this)
        isExpandedLocal = isExpanded
    }
}*/