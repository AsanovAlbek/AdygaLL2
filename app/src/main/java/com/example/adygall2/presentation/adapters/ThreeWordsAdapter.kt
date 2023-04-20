package com.example.adygall2.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.databinding.SimpleWordItemBinding
import com.example.adygall2.domain.model.ComplexAnswer

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае с 3 словами, из которых
 * надо выбрать подходящий перевод)
 *
 * @param context - контекст фрагмента
 * @param itemsList - список ответов
 * @param listener - слушатель выбора элемента
 */

class ThreeWordsAdapter(
    private val context : Context,
    private val itemsList : List <ComplexAnswer>,
    private val listener : ((ComplexAnswer) -> Unit)
) : RecyclerView.Adapter<ThreeWordsAdapter.ThreeWordsHolder>() {

    /** Позиция выбранного элемента */
    private var selectItemPosition = -1

    inner class ThreeWordsHolder (
        private val itemBinding : SimpleWordItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(answer: ComplexAnswer) {
            itemBinding.simpleWord.text = answer.answer.answer

            if (selectItemPosition == -1) {
                // Если элемент не выбран, то фон белый
                itemBinding.simpleWordContainer.setCardBackgroundColor(Color.WHITE)
            }
            else {
                if (selectItemPosition == bindingAdapterPosition) {
                    // Если позиция выбранного элемента совпала с позицией адаптера, то меняем цвет
                    // на зелённый
                    itemBinding.simpleWordContainer.setCardBackgroundColor(context.resources.getColor(R.color.lavender_blue, null))
                }
                else {
                    // Иначе снова белый
                    itemBinding.simpleWordContainer.setCardBackgroundColor(Color.WHITE)
                }
            }

            itemBinding.simpleWordContainer.setOnClickListener {
                // Если позиция адаптера не равна выбранной, то оповещаем об этом адаптер,
                // после чего меняем выбранную позицию на позицию адаптера
                if (selectItemPosition != bindingAdapterPosition) {
                    notifyItemChanged(selectItemPosition)
                    selectItemPosition = bindingAdapterPosition
                    notifyItemChanged(selectItemPosition)
                    listener.invoke(answer)
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