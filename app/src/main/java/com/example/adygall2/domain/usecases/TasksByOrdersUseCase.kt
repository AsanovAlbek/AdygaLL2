package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Класс для реализации логики, связанной с заданиями
 */
class TasksByOrdersUseCase : KoinComponent {
    private val repository : Repository by inject()
    suspend operator fun invoke(orders: List<Order>) = repository.getTasksFromOrder(orders)
}