package org.alexcawl.todoapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.alexcawl.todoapp.domain.model.DataState

interface SynchronizeUseCase {
    operator fun invoke(): Flow<DataState<Boolean>>
}