package com.example.adygall2.presentation.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.*
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.databinding.ImageQuestionItemBinding
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае с картинками вариантов ответов)
 */

class ImageAdapter(
    private val pictures : List<Picture>,
    private val listener : OnSelectClickListener
) : RecyclerView.Adapter<ImageAdapter.ImageQueHolder>() {

    // Переменная для хранения позиции выбранного элемента
    private var selectItemPosition = -1

    /**
     * inner class - вложенный класс, имеющий доступ к переменным и методам внешнего класса
     * Класс - Holder, нужен для взаимодействия с элементами внутри элемента адаптера
     * @param itemView - root элемент из которого берутся его различные view
     */

    interface OnSelectClickListener {
        fun onSelect(position : Int, picture : Picture)
    }

    inner class ImageQueHolder(
        private val itemBinding : ImageQuestionItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        // Метод для изменения элементов внутри элемента адаптера, а так же присвоение им действий
        fun binding(picture : Picture) {
            if (selectItemPosition == -1) {
                itemBinding.pictureCard.setCardBackgroundColor(WHITE)
                itemBinding.wordInCard.setTextColor(BLACK)
            }
            else {
                if (selectItemPosition == adapterPosition) {
                    // Если позиция выбранного элемента совпала с позицией адаптера, то меняем цвет
                        // на зелённый
                    // Если элемент не выбран, то фон белый
                    itemBinding.pictureCard.setCardBackgroundColor(GREEN)
                    itemBinding.wordInCard.setTextColor(WHITE)
                }
                else {
                    // Иначе снова белый
                    itemBinding.pictureCard.setCardBackgroundColor(WHITE)
                    itemBinding.wordInCard.setTextColor(BLACK)
                }
            }

            // Ставим картинку
            val bitmapPic = BitmapFactory
                .decodeByteArray(picture.picture, 0, picture.picture.size)
            itemBinding.pictureInCard.setImageBitmap(bitmapPic)

            // Ставим слово к картинке
            itemBinding.wordInCard.text = picture.name

            // Присваиваем root элементу обработчик нажатий
            itemBinding.pictureCard.setOnClickListener {
                // Если позиция адаптера не равна выбранной, то оповещаем об этом адаптер,
                // после чего меняем выбранную позицию на позицию адаптера
                notifyItemChanged(selectItemPosition)
                if (selectItemPosition != adapterPosition) {
                    selectItemPosition = adapterPosition
                    notifyItemChanged(selectItemPosition)
                }
                listener.onSelect(adapterPosition, picture)
                Log.i(MY_LOG_TAG, "select = $selectItemPosition, adapterPos = $adapterPosition")
            }
        }
    }

    fun getSelectedItem() : Picture? {
        if (selectItemPosition != -1) {
            return pictures[selectItemPosition]
        }
        return null
    }

    // Метод для создания ViewHolder, в котором мы объявляем его элемент
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageQueHolder(
            ImageQuestionItemBinding.bind(
                LayoutInflater.from(parent.context).
                        inflate(R.layout.image_question_item, parent, false)
            )
        )

    // Метод для заполнения ViewHolder
    override fun onBindViewHolder(holder: ImageQueHolder, position: Int) {
        holder.binding(pictures[position])

        Log.i(MY_LOG_TAG, pictures.toString())

    }

    // Получение количества элементов в адаптере
    override fun getItemCount() = pictures.size
}