package org.alexcawl.todoapp.data.database.db

import android.content.Context

object DatabaseModule {
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    val appDatabase: ApplicationDatabase by lazy {
        ApplicationDatabase.getInstance(applicationContext)
    }
}