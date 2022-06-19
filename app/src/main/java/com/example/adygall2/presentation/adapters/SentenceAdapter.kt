package com.example.adygall2.presentation.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.databinding.SentenceItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае со словами, из которых надо
 * составить предложение)
 */

class SentenceAdapter(
    private val answers : List<Answer>,
) : RecyclerView.Adapter<SentenceAdapter.SentenceHolder>() {

    var observerAdapter : SecondSentenceAdapter? = null

    fun registerObserverAdapter(adapter : SecondSentenceAdapter) {
        observerAdapter = adapter
    }

    fun sendWord(answer: Answer) {
        observerAdapter?.updateAnswers(answer)
    }

    private var selectedItemPos = -1

    inner class SentenceHolder(
        private val itemBinding: SentenceItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(position: Int, answer : Answer) {

            itemBinding.sentencePart.text = answer.answer
            itemBinding.sentenceWordContainer.setOnClickListener {
                if (selectedItemPos != adapterPosition) {
                    notifyItemChanged(selectedItemPos)
                    selectedItemPos = adapterPosition
                    notifyItemChanged(selectedItemPos)
                }
                sendWord(answer)
                Log.i("SentenceAdapter", "Нажато слово ${answer.answer}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceHolder =
        SentenceHolder(
            SentenceItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.sentence_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: SentenceHolder, position: Int) {
        holder.binding(position, answers[position])
    }

    override fun getItemCount() = answers.size
}