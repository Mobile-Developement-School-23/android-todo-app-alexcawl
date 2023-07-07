package org.alexcawl.todoapp.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class WorkerFactory @Inject constructor(
    private val syncWorker: SyncWorker.Factory,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> {
                syncWorker.create(appContext, workerParameters)
            }
            else -> null
        }
    }
}