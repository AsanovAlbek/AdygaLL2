package com.example.adygall2.presentation.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.SentenceItemBinding
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallbackWithAnswer
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае со словами, из которых надо
 * составить предложение)
 *
 * @param context - контекст фрагмента
 * @param isFirstAdapter - переменная для разделения двух адаптеров
 * @param answers - список ответов
 * @param callback - интерфейс для обратного вызова изменения элементов
 */

class SentenceAdapter(
    private val isFirstAdapter : Boolean,
    private val answers : MutableList<Answer>,
    private val callback: AdapterHandleDragAndDropCallbackWithAnswer
) : RecyclerView.Adapter<SentenceAdapter.SentenceHolder>() {

    /** Получение элементов адаптера */
    val adapterItems get() = answers
    var clickAction: ((Answer) -> Unit)? = null

    /** Добавление элемента ответа в адаптер
     *  @param addedAnswer - добавляемый элемент ответа
     *  @param position - позиция добавляемого элемента ответа
     */
    fun addAnswer(addedAnswer : Answer, position: Int) {
        if (!answers.contains(addedAnswer)) {
            if (position == -1) {
                val index = answers.size
                answers.add(addedAnswer)
                notifyItemInserted(index)
            }
            else {
                answers.add(position, addedAnswer)
                notifyItemInserted(position)
            }
        }
    }

    /** Удаление элемента ответа из адаптера
     *  @param removedAnswer - удаляемый элемент ответа из адаптера
     */
    fun removeAnswer(removedAnswer : Answer) {
        val index = answers.indexOf(removedAnswer)
        if (index != -1) {
            answers.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class SentenceHolder(
        private val isFirstAdapter : Boolean,
        private val itemBinding: SentenceItemBinding,
        private val callback: AdapterHandleDragAndDropCallbackWithAnswer
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(answer : Answer) {
            with(itemBinding) {
                sentenceWord.text = answer.answer
                with(root) {
                    setOnLongClickListener {
                        val item = ClipData.Item(answer.answer)
                        val dragData = ClipData(
                            answer.answer, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
                        )
                        val shadowDrag = View.DragShadowBuilder(this)
                        startDragAndDrop(dragData, shadowDrag, null, 0)
                        true
                    }

                    setOnClickListener {
                        clickAction?.invoke(answer)
                    }

                    setOnDragListener { _, dragEvent ->
                        HandleDragAndDropEvent(dragEvent).handleWithAnswer(callback, isFirstAdapter, layoutPosition)
                        true
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceHolder =
        SentenceHolder(
            isFirstAdapter,
            SentenceItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.sentence_item, parent, false
                )
            ),
            callback
        )

    override fun onBindViewHolder(holder: SentenceHolder, position: Int) {
        holder.binding(answers[position])
    }

    override fun getItemCount() = answers.size
}