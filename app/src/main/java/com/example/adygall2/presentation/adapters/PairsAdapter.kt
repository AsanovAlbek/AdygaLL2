package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.databinding.PairWordItemBinding

class PairsAdapter(
    private val answers : MutableList<String>
) : RecyclerView.Adapter<PairsAdapter.PairsHolder>() {

    private var listener : ((word : String) -> Unit)? = null

    inner class PairsHolder(
        private val itemBinding : PairWordItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(word : String) {
            itemBinding.pairWord.text = word

            itemBinding.pairWordContainer.setOnClickListener {
                answers.removeAt(adapterPosition)
                listener?.invoke(word)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairsHolder =
        PairsHolder(
            PairWordItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.pair_word_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: PairsHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size

    fun onItemClick(listener : ((word : String) -> Unit)) {
        this.listener = listener
    }
}