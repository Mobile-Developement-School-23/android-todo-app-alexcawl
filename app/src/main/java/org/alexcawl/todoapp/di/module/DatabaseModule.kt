package org.alexcawl.todoapp.di.module

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.alexcawl.todoapp.data.database.dao.TaskDao
import org.alexcawl.todoapp.data.database.datasource.DatabaseSource
import org.alexcawl.todoapp.data.database.db.ApplicationDatabase
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.domain.source.IDatabaseSource

@Module
interface DatabaseModule {
    companion object {
        @Provides
        @ApplicationScope
        fun provideDatabase(context: Context): ApplicationDatabase = Room
            .databaseBuilder(
                context,
                ApplicationDatabase::class.java,
                "task_database"
            ).build()

        @Provides
        @ApplicationScope
        fun provideTaskDao(database: ApplicationDatabase): TaskDao = database.taskDao()
    }

    @Binds
    @ApplicationScope
    fun bindDatabaseSource(source: DatabaseSource): IDatabaseSource
}