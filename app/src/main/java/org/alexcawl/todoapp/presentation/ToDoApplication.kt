package org.alexcawl.todoapp.presentation

import android.app.Application
import org.alexcawl.todoapp.di.ApplicationComponent
import org.alexcawl.todoapp.di.DaggerApplicationComponent

class ToDoApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()
    }
}