package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Класс для реализации логики, связанной с ордерами
 */
class GetAllOrdersUseCase(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(ioDispatcher) {
        repository.getAllOrders()
    }
}