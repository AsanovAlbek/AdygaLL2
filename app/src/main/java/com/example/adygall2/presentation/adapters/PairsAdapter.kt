package com.example.adygall2.presentation.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.PairWordItemBinding
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import kotlin.math.abs

/**
 * Адаптер для пар ответов
 * @param context - контекст фрагмента
 * @param isFirstAdapter - переменная для разделения адаптеров
 * @param answers - список ответов
 * @param callback - обратный вызов для изменения элементов в адаптере
 */
class PairsAdapter(
    private val context: Context,
    private val isFirstAdapter: Boolean,
    private val answers: MutableList<String>,
    private val callback: AdapterHandleDragAndDropCallback
) : RecyclerView.Adapter<PairsAdapter.PairsHolder>() {

    /** Переменная для получения всех элементов */
    val adapterItems get() = answers
    var clickEvent: ((String) -> Unit)? = null

    /**
     * Метод для удаления элемента для его перемещения в другой PairsAdapter
     * @param answer - передаваемый элемент
     */
    fun removeAnswer(answer: String) {
        val index = answers.indexOf(answer)
        if (index != -1) {
            answers.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    /**
     * Метод для добавления элемента в PairsAdapter
     * @param answer - добавляемый элемент
     * @param position - позиция, в которую он передаётся
     */
    fun addAnswer(answer: String, position: Int) {
        // Если в списке не имеется данного элемента
        if (!answers.contains(answer)) {
            // Если он передан в пустое место в RecyclerView
            if (position == -1) {
                // Добавляем его в конец
                val index = answers.size
                answers.add(answer)
                notifyItemInserted(index)
            } else {
                // Добавляем его по позиции
                answers.add(position, answer)
                notifyItemInserted(position)
            }
        }
    }

    inner class PairsHolder(
        private val context: Context,
        private val isFirstAdapter: Boolean,
        private val itemBinding: PairWordItemBinding,
        private val callback: AdapterHandleDragAndDropCallback
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(word: String) {
            with(itemBinding) {
                pairWord.text = word
                setCardParams()
                // Обработка долгого нажатия
                with(root) {
                    setOnLongClickListener { view ->
                        val item = ClipData.Item(word)
                        val dragData = ClipData(
                            word, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
                        )
                        val shadowDrag = View.DragShadowBuilder(this)
                        // Начало процесса перемещения
                        startDragAndDrop(dragData, shadowDrag, null, 0)
                        true
                    }

                    // Обработка перемещения
                    setOnDragListener { _, dragEvent ->
                        Log.i("game", "drag")
                        HandleDragAndDropEvent(dragEvent).handle(
                            callback,
                            isFirstAdapter,
                            layoutPosition
                        )
                        true
                    }

                    setOnClickListener {
                        Log.i("game", "click")
                        clickEvent?.invoke(word)
                    }
                }
            }
        }

        /** Метод для смены параметров карточки */
        private fun setCardParams() {
            with(itemBinding) {
                pairWordContainer.setCardBackgroundColor(
                    context.resources.getColor(
                        R.color.unbleached_silk,
                        null
                    )
                )
                pairWordContainer.strokeColor = context.resources.getColor(R.color.orange, null)
                pairWordContainer.strokeWidth = 1
                pairWord.setTextColor(context.resources.getColor(R.color.orange, null))
                root.invalidate()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairsHolder =
        PairsHolder(
            context = context,
            isFirstAdapter = isFirstAdapter,
            itemBinding = PairWordItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.pair_word_item, parent, false
                )
            ),
            callback = callback
        )

    override fun onBindViewHolder(holder: PairsHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size
}