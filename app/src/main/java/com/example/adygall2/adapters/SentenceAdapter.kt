package com.example.adygall2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R

class SentenceAdapter(
    private val words : List<String>
) : RecyclerView.Adapter<SentenceAdapter.SentenceHolder>() {
    inner class SentenceHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val container : LinearLayout = itemView.findViewById(R.id.sentence_word_container)
        val wordTv : TextView = itemView.findViewById(R.id.sentence_part)

        fun binding(word : String) {
            wordTv.text = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sentence_item, parent, false)
        return SentenceHolder(itemView)
    }

    override fun onBindViewHolder(holder: SentenceHolder, position: Int) {
        holder.binding(words[position])
    }

    override fun getItemCount() = words.size
}