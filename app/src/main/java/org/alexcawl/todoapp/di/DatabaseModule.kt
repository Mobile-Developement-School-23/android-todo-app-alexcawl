package org.alexcawl.todoapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.db.ApplicationDatabase
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import javax.inject.Singleton

@Module
class DatabaseModule {
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
    fun provideDatabaseSource(committer: PreferencesCommitter, dao: TaskDao): DatabaseSource = DatabaseSource(committer, dao)
}