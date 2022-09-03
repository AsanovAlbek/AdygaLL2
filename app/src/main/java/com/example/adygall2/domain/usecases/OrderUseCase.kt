package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

/**
 * Класс для реализации логики, связанной с ордерами
 */
class OrderUseCase(
    private val repository: Repository
) {
    fun getAllOrders() = repository.getAllOrders()
}