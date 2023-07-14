package org.alexcawl.todoapp.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.domain.repository.IAlarmScheduler
import org.alexcawl.todoapp.presentation.receiver.AlarmScheduler
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
            8, TimeUnit.HOURS,
            7, TimeUnit.HOURS
        ).setConstraints(constraints).addTag("sync_data").build()

        @ApplicationScope
        @Provides
        fun provideScope(): CoroutineScope = CoroutineScope(SupervisorJob())
    }

    @ApplicationScope
    @Binds
    fun provideAlarmScheduler(manager: AlarmScheduler): IAlarmScheduler
}