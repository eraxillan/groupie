package com.xwray.groupie

object GroupUtils1 {
    fun <T: Group1> getItem(groups: Collection<T>, position: Int): Item1<*> {
        var previousPosition = 0

        for (group in groups) {
            val size = group.getItemCount()
            if (size + previousPosition > position) {
                return group.getItem(position - previousPosition)
            }
            previousPosition += size
        }

        throw IndexOutOfBoundsException("Wanted item at $position but there are only $previousPosition items")
    }

    fun <T: Group1> getItemCount(groups: Collection<T>): Int {
        var size = 0
        for (group in groups) {
            size += group.getItemCount()
        }
        return size
    }
}