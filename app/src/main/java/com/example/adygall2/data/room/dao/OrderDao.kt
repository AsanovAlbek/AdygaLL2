package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.entities.OrderEntity

/**
 * DAO для запросов, связанных с ордерами [OrderEntity]
 */
@Dao
abstract class OrderDao {
    /** Получить [OrderEntity] по id */
    @Query("SELECT * FROM `order` WHERE id = :orderId")
    abstract fun getOrder(orderId : Int) : OrderEntity

    /** Получить все [OrderEntity] */
    @Query("SELECT * FROM `order`")
    abstract fun getAllOrders() : List<OrderEntity>
}