package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.SentenceItemBinding

/**
 * Адаптер для помещения слова в ячейку
 * @param answers - список ответов
 */
class SimpleSentenceAdapter(
    private val answers: MutableList<String>
) : RecyclerView.Adapter<SimpleSentenceAdapter.SimpleSentenceHolder>() {

    /** Обратный вызов ответа */
    private var callback : ((String) -> Unit)? = null

    inner class SimpleSentenceHolder(
        private val itemBinding: SentenceItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(answer : String) {
            itemBinding.apply {
                sentenceWord.text = answer
                root.setOnClickListener {
                    callback?.invoke(answer)
                }
            }
        }
    }

    fun removeAnswer(answer : String) {
        val removedIndex = answers.indexOf(answer)
        if (removedIndex != -1) {
            answers.removeAt(removedIndex)
            notifyItemRemoved(removedIndex)
        }
    }

    fun addAnswer(answer: String) {
        val addIndex = answers.size
        answers.add(answer)
        notifyItemInserted(addIndex)
    }

    fun setListener(listener : ((String) -> Unit)) {
        callback = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleSentenceHolder =
        SimpleSentenceHolder(
            SentenceItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.sentence_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: SimpleSentenceHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size
}