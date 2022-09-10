package com.example.adygall2.presentation.adapters.adapter_handles

/**
 * Интерфейс для реализаций обратного вызова в адаптере
 */
interface AdapterHandleDragAndDropCallback {
    /** Вызов изменения элементов адаптера при перетаскивании */
    fun change(isFirstAdapter: Boolean, item : String, position: Int)
}