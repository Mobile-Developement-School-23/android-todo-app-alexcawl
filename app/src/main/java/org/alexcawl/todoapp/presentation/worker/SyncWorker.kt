package org.alexcawl.todoapp.presentation.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.alexcawl.todoapp.data.util.NetworkException
import org.alexcawl.todoapp.domain.repository.ISynchronizer

class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val synchronizer: ISynchronizer
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            Log.d("WORKER-HERE", "hey bro I'm working!")
            synchronizer.synchronize()
            Result.success()
        } catch (exception: NetworkException) {
            Result.retry()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, workerParameters: WorkerParameters): SyncWorker
    }
}