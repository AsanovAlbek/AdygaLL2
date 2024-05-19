package com.example.adygall2.presentation.adapters.adapter_handles

import com.example.adygall2.domain.model.Answer

/**
 * Интерфейс для реализаций обратного вызова в адаптере
 */
interface AdapterHandleDragAndDropCallback {
    /** Вызов изменения элементов адаптера при перетаскивании */
    fun change(
        isFirstAdapter: Boolean,
        item: String,
        position: Int
    )
}

interface AdapterHandleDragAndDropCallbackWithAnswer {
    fun changeWithAnswer(
        isFirstAdapter: Boolean,
        item: Answer,
        position: Int
    )
}