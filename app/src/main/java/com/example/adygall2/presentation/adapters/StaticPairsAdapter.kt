package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.PairWordItemBinding

/**
 * Адаптер для статичного Recycler View в задании с парами слов
 * @param answers - список ответов
 */
class StaticPairsAdapter(
    private val answers : MutableList<String>
) : RecyclerView.Adapter<StaticPairsAdapter.StaticPairsViewHolder>() {

    val adapterItems get() = answers
    inner class StaticPairsViewHolder(private val itemBinding: PairWordItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
                fun bind(answer : String) {
                    itemBinding.pairWord.text = answer
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaticPairsViewHolder =
        StaticPairsViewHolder(
            PairWordItemBinding.bind( LayoutInflater.from(parent.context).inflate(
                R.layout.pair_word_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: StaticPairsViewHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size
}