package com.example.adygall2.presentation.adapters.adapter_handles

import android.content.ClipData
import android.view.DragEvent

/**
 * Вспомогательный класс для перетаскивания элемента
 */
class HandleDragAndDropEvent(private val event : DragEvent) {
    fun handle(callback: AdapterHandleDragAndDropCallback, isFirstAdapter: Boolean, position: Int) {
        if (event.action == DragEvent.ACTION_DROP) {
            val item : ClipData.Item = event.clipData.getItemAt(0)
            val dragData = item.text.toString()
            callback.change(isFirstAdapter, dragData, position)
        }
    }
}