package org.alexcawl.todoapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.db.ApplicationDatabase
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): ApplicationDatabase = Room
        .databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "task_database"
        ).build()

    @Provides
    @Singleton
    fun provideTaskDao(database: ApplicationDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideDatabaseSource(dao: TaskDao): DatabaseSource = DatabaseSource(dao)
}