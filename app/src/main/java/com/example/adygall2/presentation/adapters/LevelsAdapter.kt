package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.databinding.FragmentLevelPageBinding

class LevelsAdapter(
    private val tasks : List<Task>,
    private val flagClickEvent : ((List<Task>) -> Unit)
) : RecyclerView.Adapter<LevelsAdapter.LevelHolder>() {

    private val flagsList = tasks.chunked(15)

    inner class LevelHolder(
        private val itemBinding: FragmentLevelPageBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {

            itemBinding.testFlag1.textUnderFlag.text = "Задание ${position+1}"
            itemBinding.testFlag1.flag.setOnClickListener {
                flagClickEvent.invoke(flagsList[position])
            }

            itemBinding.testFlag2.textUnderFlag.text = "Задание ${position+2}"
            itemBinding.testFlag2.flag.setOnClickListener {
                flagClickEvent.invoke(flagsList[position + 1])
            }

            itemBinding.testFlag3.textUnderFlag.text = "Задание ${position+3}"
            itemBinding.testFlag3.flag.setOnClickListener {
                flagClickEvent.invoke(flagsList[position + 2])
            }

            itemBinding.testFlag4.textUnderFlag.text = "Задание ${position+4}"
            itemBinding.testFlag4.flag.setOnClickListener {
                flagClickEvent.invoke(flagsList[position + 3])
            }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelHolder =
        LevelHolder(
            FragmentLevelPageBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_level_page, parent, false)
            )
        )

    override fun onBindViewHolder(holder: LevelHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = tasks.size
}