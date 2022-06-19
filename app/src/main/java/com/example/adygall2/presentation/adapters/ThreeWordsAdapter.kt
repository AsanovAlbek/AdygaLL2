package com.example.adygall2.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.SimpleWordItemBinding

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае с 3 словами, из которых
 * надо выбрать подходящий перевод)
 */

class ThreeWordsAdapter(
    // Лист со словами
    private val itemsList : List <Answer>
) : RecyclerView.Adapter<ThreeWordsAdapter.ThreeWordsHolder>() {

    private var selectItemPosition = -1

    inner class ThreeWordsHolder (
        private val itemBinding : SimpleWordItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(answer: Answer) {
            itemBinding.simpleWord.text = answer.answer

            if (selectItemPosition == -1) {
                // Если элемент не выбран, то фон белый
                itemBinding.simpleWordContainer.setCardBackgroundColor(Color.WHITE)
            }
            else {
                if (selectItemPosition == adapterPosition) {
                    // Если позиция выбранного элемента совпала с позицией адаптера, то меняем цвет
                    // на зелённый
                    itemBinding.simpleWordContainer.setCardBackgroundColor(Color.GREEN)
                }
                else {
                    // Иначе снова белый
                    itemBinding.simpleWordContainer.setCardBackgroundColor(Color.WHITE)
                }
            }

            itemBinding.simpleWordContainer.setOnClickListener {
                // Если позиция адаптера не равна выбранной, то оповещаем об этом адаптер,
                // после чего меняем выбранную позицию на позицию адаптера
                if (selectItemPosition != adapterPosition) {
                    notifyItemChanged(selectItemPosition)
                    selectItemPosition = adapterPosition
                    notifyItemChanged(selectItemPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreeWordsHolder =
        ThreeWordsHolder(
            SimpleWordItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.simple_word_item, parent, false
                )
            )
        )

    override fun onBindViewHolder(holder: ThreeWordsHolder, position: Int) {
        holder.binding(itemsList[position])
    }

    override fun getItemCount() = itemsList.size

}