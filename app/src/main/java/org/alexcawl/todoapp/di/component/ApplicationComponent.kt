package org.alexcawl.todoapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.alexcawl.todoapp.di.module.*
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.presentation.ToDoApplication
import org.alexcawl.todoapp.presentation.receiver.PostponeReceiver
import org.alexcawl.todoapp.presentation.receiver.NotificationReceiver

@ApplicationScope
@Component(modules = [DatabaseModule::class, NetworkModule::class, RepositoryModule::class, AndroidModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun mainActivityComponent(): MainActivityComponent

    fun inject(application: ToDoApplication)

    fun inject(receiver: NotificationReceiver)

    fun inject(receiver: PostponeReceiver)
}