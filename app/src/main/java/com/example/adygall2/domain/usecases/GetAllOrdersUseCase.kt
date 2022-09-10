package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Класс для реализации логики, связанной с ордерами
 */
class GetAllOrdersUseCase : KoinComponent {
    private val repository : Repository by inject()
    suspend operator fun invoke() = repository.getAllOrders()
}