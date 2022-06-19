package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.SentenceItemBinding

class SecondSentenceAdapter(
    private val answers : MutableList<Answer>,
    private val subscribeAdapter : SentenceAdapter
) : RecyclerView.Adapter<SecondSentenceAdapter.SecondSentenceHolder>() {

    init {
        subscribeAdapter.registerObserverAdapter(this)
    }
    val answerList get() = answers

    fun updateAnswers(answer: Answer) {
        answers.add(answer)
        notifyDataSetChanged()
    }

    inner class SecondSentenceHolder(private val itemBinding: SentenceItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            fun binding(position: Int, answer: Answer) {
                itemBinding.sentencePart.text = answer.answer
                itemBinding.sentenceWordContainer.setOnClickListener {
                    answers.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondSentenceHolder =
        SecondSentenceHolder(
            SentenceItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.sentence_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: SecondSentenceHolder, position: Int) {
        holder.binding(position, answers[position])
    }

    override fun getItemCount() = answers.size

    fun getItems() = answers

}