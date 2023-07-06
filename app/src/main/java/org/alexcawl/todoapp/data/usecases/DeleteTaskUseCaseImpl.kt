package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.model.TaskModel
import org.alexcawl.todoapp.domain.repository.TaskLocalRepository
import org.alexcawl.todoapp.domain.usecases.DeleteTaskUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTaskUseCaseImpl @Inject constructor(
    private val repository: TaskLocalRepository
) : DeleteTaskUseCase {
    override suspend operator fun invoke(task: TaskModel): Flow<DataState<Boolean>> = flow {
        emit(DataState.Initial)
        repository.deleteTask(task)
        emit(DataState.Result(true))
    }.catch {
        emit(DataState.Exception(it))
    }
}