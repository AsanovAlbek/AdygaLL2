package com.example.adygall2.domain.model

/**
 * Модель Order для задания очерёдности заданиям
 * @param id - id ордера
 * @param taskNum - id задания
 */
data class Order(val id : Int, val taskNum : Int)