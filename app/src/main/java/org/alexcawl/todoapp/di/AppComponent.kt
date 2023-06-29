package org.alexcawl.todoapp.di

import dagger.Component
import org.alexcawl.todoapp.presentation.fragment.TaskAddFragment
import org.alexcawl.todoapp.presentation.fragment.TaskEditFragment
import org.alexcawl.todoapp.presentation.fragment.TaskShowFragment
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [AppModule::class, DatabaseModule::class, DomainModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(fragment: TaskShowFragment)

    fun inject(fragment: TaskAddFragment)

    fun inject(fragment: TaskEditFragment)
}