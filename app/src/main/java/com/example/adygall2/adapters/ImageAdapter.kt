package com.example.adygall2.adapters

import android.graphics.Color.WHITE
import android.graphics.Color.GREEN
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.models.ImageTask

class ImageAdapter(
    private val imageQueItems : List<ImageTask>
) : RecyclerView.Adapter<ImageAdapter.ImageQueHolder>() {

    private var selectItemPosition = -1

    inner class ImageQueHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val container : CardView = itemView.findViewById(R.id.picture_card)
        val image : ImageView = itemView.findViewById(R.id.picture_in_card)

        fun binding(answer: ImageTask) {
            if (selectItemPosition == -1) {
                container.setCardBackgroundColor(WHITE)
            }
            else {
                if (selectItemPosition == adapterPosition) {
                    container.setCardBackgroundColor(GREEN)
                }
                else {
                    container.setCardBackgroundColor(WHITE)
                }
            }

            image.setImageResource(answer.image)

            itemView.setOnClickListener {
                if (selectItemPosition != adapterPosition) {
                    notifyItemChanged(selectItemPosition)
                    selectItemPosition = adapterPosition
                    notifyItemChanged(selectItemPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageQueHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.image_question_item, parent, false)
        return ImageQueHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageQueHolder, position: Int) {
        holder.binding(imageQueItems[position])
    }

    override fun getItemCount() = imageQueItems.size
}