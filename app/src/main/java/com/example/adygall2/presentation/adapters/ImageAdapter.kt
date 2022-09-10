package com.example.adygall2.presentation.adapters

import android.content.Context
import android.graphics.Color.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.ImageQuestionItemBinding
import com.example.adygall2.domain.model.ComplexAnswer

/**
 * Класс - Адаптер, наследуемый от класса Adapter из класса RecyclerView
 * Нужен для взаимодействия с данными в списках (в нашем случае с картинками вариантов ответов)
 *
 * @param context - контекст фрагмента
 * @param complexAnswerList - лист из ответов, озвучек и картинок
 * @param listener - слушатель выбора карточки с картинкой
 */

class ImageAdapter(
    private val context: Context,
    private val complexAnswerList : List<ComplexAnswer>,
    private val listener : ((ComplexAnswer) -> Unit)
) : RecyclerView.Adapter<ImageAdapter.ImageQueHolder>() {

    /** Переменная для хранения позиции выбранного элемента */
    private var selectItemPosition = -1

    /** Обратный вызов при уничтожении фрагмента, нужен для своевременной очистки кэша [Glide] */
    private var onDestroyCallback : (() -> Unit)? = null

    /**
     * inner class - вложенный класс, имеющий доступ к переменным и методам внешнего класса
     * Класс - Holder, нужен для взаимодействия с элементами внутри элемента адаптера
     * @param itemBinding - переменная для взаимодействия с элементом списка
     */
    inner class ImageQueHolder(
        private val itemBinding : ImageQuestionItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        // Метод для изменения элементов внутри элемента адаптера, а так же присвоение им действий
        fun binding(answer: ComplexAnswer) {

            // Находим картинку к варианту ответа
            val answerPicture = answer.answerPicture

            // Ставим картинку
            Glide.with(itemBinding.root.context)
                .load(answerPicture.source)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(itemBinding.pictureInCard)

            // Ставим слово к картинке
            itemBinding.wordInCard.text = answerPicture.name

            // Находим звук к варианту ответа
            val answerSound = answer.answerSound
            val soundsPlayer = SoundsPlayer(context)

            if (selectItemPosition == -1) {
                itemBinding.pictureCard.setCardBackgroundColor(WHITE)
                itemBinding.wordInCard.setTextColor(BLACK)
            }
            else {
                if (selectItemPosition == adapterPosition) {
                    // Если позиция выбранного элемента совпала с позицией адаптера, то меняем цвет
                        // на зелённый
                    // Если элемент не выбран, то фон белый
                    itemBinding.pictureCard.setCardBackgroundColor(context.resources.getColor(R.color.lavender_blue, null))
                    itemBinding.wordInCard.setTextColor(WHITE)
                }
                else {
                    // Иначе снова белый
                    itemBinding.pictureCard.setCardBackgroundColor(WHITE)
                    itemBinding.wordInCard.setTextColor(BLACK)
                }
            }

            // Присваиваем root элементу обработчик нажатий
            itemBinding.pictureCard.setOnClickListener {
                    // Если позиция адаптера не равна выбранной, то оповещаем об этом адаптер,
                    // после чего меняем выбранную позицию на позицию адаптера
                    notifyItemChanged(selectItemPosition)
                    if (selectItemPosition != adapterPosition) {
                        selectItemPosition = adapterPosition
                        notifyItemChanged(selectItemPosition)
                    }
                    if (soundsPlayer.isPlayingNow) {
                        soundsPlayer.stopPlay()
                    }
                    soundsPlayer.playSound(answerSound)
                    listener.invoke(answer)

            }
        }

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
        holder.binding(complexAnswerList[position])
    }

    // Получение количества элементов в адаптере
    override fun getItemCount() = complexAnswerList.size

    fun onDestroy(callback : (() -> Unit)) {
        onDestroyCallback = callback
        onDestroyCallback?.invoke()
    }
}