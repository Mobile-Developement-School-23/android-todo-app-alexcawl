package org.alexcawl.todoapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.alexcawl.todoapp.di.module.AndroidModule
import org.alexcawl.todoapp.di.module.DatabaseModule
import org.alexcawl.todoapp.di.module.NetworkModule
import org.alexcawl.todoapp.di.module.RepositoryModule
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.presentation.ToDoApplication

@ApplicationScope
@Component(modules = [DatabaseModule::class, NetworkModule::class, RepositoryModule::class, AndroidModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun mainActivityComponent(): MainActivityComponent

    fun inject(application: ToDoApplication)
}