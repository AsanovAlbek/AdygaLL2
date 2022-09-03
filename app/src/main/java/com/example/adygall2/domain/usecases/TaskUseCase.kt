package com.example.adygall2.domain.usecases

import com.example.adygall2.data.db_models.Order
import com.example.adygall2.domain.repository.Repository

/**
 * Класс для реализации логики, связанной с заданиями
 */
class TaskUseCase(
    private val repository: Repository
) {
    fun getTasksFromOrders(orders : List<Order>) =
        repository.getTasksFromOrder(orders)
}