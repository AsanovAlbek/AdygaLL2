package com.example.adygall2.presentation.adapters.adapter_handles

import android.content.ClipData
import android.util.Log
import android.view.DragEvent
import com.example.adygall2.domain.model.Answer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Вспомогательный класс для перетаскивания элемента
 */
class HandleDragAndDropEvent(private val event : DragEvent) {

    fun handle(callback: AdapterHandleDragAndDropCallback, isFirstAdapter: Boolean, position: Int) {

        when(event.action) {
            DragEvent.ACTION_DROP -> {
                val item : ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()
                callback.change(isFirstAdapter = isFirstAdapter, item = dragData, position = position)
                Log.i("drag and drop", "view dropped")
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.i("drag and drop", "drag started")
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                /*val item : ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()

                callback.change(isFirstAdapter, dragData, position)*/
                Log.i("drag and drop", "drag ended")
            }

        }
    }

    fun handleWithAnswer(callbackWithAnswer: AdapterHandleDragAndDropCallbackWithAnswer, isFirstAdapter: Boolean, position: Int) {
        when(event.action) {
            DragEvent.ACTION_DROP -> {
                val item : ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()
                val answerFromDragData = Json.decodeFromString<Answer>(dragData)
                callbackWithAnswer.changeWithAnswer(isFirstAdapter = isFirstAdapter, item = answerFromDragData, position = position)
                Log.i("drag and drop", "view dropped")
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.i("drag and drop", "drag started")
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.i("drag and drop", "drag ended")
            }

        }
    }
}