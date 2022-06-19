package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

class OrderUseCase(
    private val repository: Repository
) {

    fun getAllOrders() = repository.getAllOrders()

    fun getOrderById(orderId : Int) = repository.getOrderById(orderId)

}