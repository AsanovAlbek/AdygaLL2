package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst
import com.example.adygall2.data.room.consts.RoomConst.ORDER_TABLE_NAME
import com.example.adygall2.data.room.entities.OrderEntity

@Dao
abstract class OrderDao {
    @Query("SELECT * FROM `order` WHERE id = :orderId")
    abstract fun getOrder(orderId : Int) : OrderEntity

    @Query("SELECT * FROM `order`")
    abstract fun getAllOrders() : List<OrderEntity>
}