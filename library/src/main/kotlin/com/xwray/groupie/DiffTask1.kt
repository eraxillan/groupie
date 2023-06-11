package com.xwray.groupie

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.DiffUtil
import java.lang.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DiffTask1(
    asyncDiffUtil: AsyncDiffUtil1,
    private val diffCallback: DiffUtil.Callback,
    private val runGeneration: Int,
    private val detectMoves: Boolean,
    onAsyncUpdateListener: OnAsyncUpdateListener1?
): CoroutineAsyncTask<Unit, DiffUtil.DiffResult?> {

    private val asyncDiffUtilLocal: WeakReference<AsyncDiffUtil1> = WeakReference(asyncDiffUtil)
    private val onAsyncUpdateListenerLocal: WeakReference<OnAsyncUpdateListener1> = WeakReference(onAsyncUpdateListener)

    private var backgroundException: Exception? = null

    override fun onPreExecute() {}

    override fun doInBackground(vararg params: Unit): DiffUtil.DiffResult? {
        return try {
            DiffUtil.calculateDiff(diffCallback, detectMoves)
        } catch (exc: Exception) {
            backgroundException = exc
            null
        }
    }

    override fun onPostExecute(result: DiffUtil.DiffResult?) {
        if (backgroundException != null) {
            throw RuntimeException(backgroundException)
        }

        val async = asyncDiffUtilLocal.get() ?: return
        if (shouldDispatchResult(result, async)) {
            async.asyncDiffUtilCallback.onDispatchAsyncResult(async.groups)
            result?.dispatchUpdatesTo(async.asyncDiffUtilCallback)
            onAsyncUpdateListenerLocal.get()?.onUpdateComplete()
        }
    }

    private fun shouldDispatchResult(diffResult: DiffUtil.DiffResult?, async: AsyncDiffUtil1?): Boolean {
        return diffResult != null && async != null && runGeneration == async.maxScheduledGeneration
    }
}

internal fun <P, R> CoroutineScope.executeAsyncTask(task: CoroutineAsyncTask<P, R>) = launch {
    task.onPreExecute()

    val result = withContext(Dispatchers.IO) {
        task.doInBackground()
    }

    task.onPostExecute(result)
}

internal interface CoroutineAsyncTask<Params, Result> {
    @WorkerThread
    fun doInBackground(vararg params: Params): Result

    @MainThread
    fun onPreExecute() {}

    @MainThread
    fun onPostExecute(result: Result) {}
}