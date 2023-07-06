package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.Priority
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.AddTaskUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddTaskUseCaseImpl @Inject constructor(
    private val repository: TaskLocalRepository
) : AddTaskUseCase {
    override suspend fun invoke(text: String, priority: Priority, deadline: Long?): Flow<DataState<Boolean>> = flow {
        emit(DataState.Initial)
        repository.addTask(text, priority, deadline)
        emit(DataState.Result(true))
    }.catch {
        emit(DataState.Exception(it))
    }
}