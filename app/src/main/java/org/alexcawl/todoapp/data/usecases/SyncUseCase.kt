package org.alexcawl.todoapp.data.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.alexcawl.todoapp.domain.model.DataState
import org.alexcawl.todoapp.domain.repository.ISynchronizer
import org.alexcawl.todoapp.domain.usecases.ISyncUseCase
import javax.inject.Inject

class SyncUseCase @Inject constructor(
    private val repository: ISynchronizer
) : ISyncUseCase {
    override fun invoke(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Initial)
        repository.synchronize()
        emit(DataState.Result(true))
    }.catch {
        emit(DataState.Exception(it))
    }
}