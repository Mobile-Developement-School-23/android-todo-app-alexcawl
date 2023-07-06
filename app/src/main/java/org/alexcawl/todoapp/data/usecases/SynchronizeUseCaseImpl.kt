package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.repository.Synchronizer
import org.alexcawl.todoapp.domain.usecases.SynchronizeUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SynchronizeUseCaseImpl @Inject constructor(
    private val repository: Synchronizer
): SynchronizeUseCase {
    override fun invoke(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Initial)
        repository.synchronize()
        emit(DataState.Result(true))
    }.catch {
        emit(DataState.Exception(it))
    }
}