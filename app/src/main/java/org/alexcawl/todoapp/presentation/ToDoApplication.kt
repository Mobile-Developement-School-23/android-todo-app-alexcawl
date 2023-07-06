package org.alexcawl.todoapp.presentation

import android.app.Application
import org.alexcawl.todoapp.di.AppComponent
import org.alexcawl.todoapp.di.AppModule
import org.alexcawl.todoapp.di.DaggerAppComponent

class ToDoApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}