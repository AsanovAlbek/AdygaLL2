package com.example.adygall2.presentation.adapters.adapter_handles

import com.example.adygall2.data.db_models.Answer

/**
 * Интерфейс для реализаций обратного вызова в адаптере
 */
interface AdapterCallback {
    /** Вызов изменения элементов адаптера при перетаскивании */
    fun change(isFirstAdapter: Boolean, item : String, position: Int)
}