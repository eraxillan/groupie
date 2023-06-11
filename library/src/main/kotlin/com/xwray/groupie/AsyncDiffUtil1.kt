package com.xwray.groupie

import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.CoroutineScope

/**
 * A wrapper around {@link DiffUtil} that calculates diff in a background thread
 */
internal class AsyncDiffUtil1(callback: Callback) {

    interface Callback : ListUpdateCallback {
        /**
         * Called on the main thread before DiffUtil dispatches the result
         */
        @MainThread
        fun onDispatchAsyncResult(newGroups: Collection<Group1>)
    }

    val asyncDiffUtilCallback: Callback = callback

    val maxScheduledGeneration: Int
        get() = maxScheduledGenerationLocal

    val groups: Collection<Group1>
        get() = groupsLocal

    private var maxScheduledGenerationLocal: Int = 0
    private var groupsLocal: Collection<Group1> = emptyList()

    fun calculateDiff(
        scope: CoroutineScope,
        newGroups: Collection<Group1>,
        diffUtilCallback: DiffUtil.Callback,
        onAsyncUpdateListener: OnAsyncUpdateListener1?,
        detectMoves: Boolean
    ) {
        groupsLocal = newGroups

        // Incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++maxScheduledGenerationLocal
        val task = DiffTask1(this, diffUtilCallback, runGeneration, detectMoves, onAsyncUpdateListener)
        scope.executeAsyncTask(task)
    }
}