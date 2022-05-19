package com.example.adygall2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.models.ImageTask

class ThreeWordsAdapter(
    private val itemsList : List <String>
) : RecyclerView.Adapter<ThreeWordsAdapter.ThreeWordsHolder>(){

    inner class ThreeWordsHolder(
        private val itemView : View
    ) : RecyclerView.ViewHolder(itemView) {
        val wordTv = itemView.findViewById<TextView>(R.id.simple_word)
        val container = itemView.findViewById<CardView>(R.id.simple_word_container)

        fun binding(word : String) {
            wordTv.text = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreeWordsHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.simple_word_item, parent, false)
        return ThreeWordsHolder(itemView)
    }

    override fun onBindViewHolder(holder: ThreeWordsHolder, position: Int) {
        holder.binding(itemsList[position])
    }

    override fun getItemCount() = itemsList.size

}