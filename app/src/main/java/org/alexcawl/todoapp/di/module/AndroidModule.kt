package org.alexcawl.todoapp.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.presentation.worker.SyncWorker
import java.util.concurrent.TimeUnit

@Module
interface AndroidModule {
    companion object {
        @ApplicationScope
        @Provides
        fun provideConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        @ApplicationScope
        @Provides
        fun provideWorkManager(
            constraints: Constraints
        ): PeriodicWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES,
            14, TimeUnit.MINUTES
        ).setConstraints(constraints).addTag("sync_data").build()
    }
}