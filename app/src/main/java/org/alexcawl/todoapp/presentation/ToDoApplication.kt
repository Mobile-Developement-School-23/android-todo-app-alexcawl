package org.alexcawl.todoapp.presentation

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import org.alexcawl.todoapp.di.component.ApplicationComponent
import org.alexcawl.todoapp.di.component.DaggerApplicationComponent
import org.alexcawl.todoapp.presentation.worker.WorkerFactory
import javax.inject.Inject

class ToDoApplication : Application() {
    private var _applicationComponent: ApplicationComponent? = null
    val applicationComponent: ApplicationComponent get() = requireNotNull(_applicationComponent!!) {
        "AppComponent must not be null!"
    }

    @Inject lateinit var workerFactory: WorkerFactory
    private val configurationWorker by lazy {
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        _applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
        println(applicationComponent.mainActivityComponent())
        WorkManager.initialize(this, configurationWorker)
    }
}