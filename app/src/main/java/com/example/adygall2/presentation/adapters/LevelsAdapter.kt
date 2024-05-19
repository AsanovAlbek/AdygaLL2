package com.example.adygall2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.local.levelsNames
import com.example.adygall2.data.room.userbase.ProgressItem
import com.example.adygall2.databinding.ItemLessonInLevelBinding
import com.example.adygall2.databinding.ItemLevelBinding
import com.example.adygall2.domain.model.LevelAndLesson

class LevelsAdapter(
    private val levelsAndLessons: List<LevelAndLesson>,
    private val lessonClickEvent: ((Int, Int) -> Unit),
    private val userProgress: Set<ProgressItem>
) : RecyclerView.Adapter<LevelsAdapter.LevelHolder>() {

    private lateinit var lessonsAdapter: LessonsAdapter

    inner class LevelHolder(private val itemBinding: ItemLevelBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int, levelTitle: String, levelsAndLesson: List<LevelAndLesson>) {
            itemBinding.apply {
                levelButton.text = levelTitle
                levelButton.setOnClickListener {
                    // Показываем и скрываем подменю по нажатию кнопки
                    val lessonsVisibility = lessonsList.isVisible
                    lessonsList.isVisible = !lessonsVisibility
                    // Соотвтетсвенно скрываем и показываем треугольник в кнопке
                    if (lessonsList.isVisible) {
                        levelButton.setIconResource(R.drawable.ic_rotated_polygon)
                    } else {
                        levelButton.setIconResource(R.drawable.ic_polygon)
                    }
                }

                val availableLessons =
                    userProgress.filter { it.level == position + 1 }.map { it.lesson }
                itemBinding.root.isEnabled = userProgress.map { it.level }.contains(position)
                val levels = levelsAndLesson.groupBy { it.level }

                // Присваиваем адаптер и подаём ему список заданий
                lessonsAdapter = LessonsAdapter(
                    lessonItemClickEvent = lessonClickEvent,
                    userProgressInLesson = availableLessons,
                    chosenLevelNum = position + 1,
                    levelsAndLesson = levels.getOrDefault(levelsAndLesson[position].level, emptyList())
                )
                lessonsList.adapter = lessonsAdapter
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LevelsAdapter.LevelHolder =
        LevelHolder(
            ItemLevelBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
            )
        )

    override fun onBindViewHolder(holder: LevelsAdapter.LevelHolder, position: Int) {
        holder.bind(
            position,
            levelTitle = levelsNames[position],
            levelsAndLesson = levelsAndLessons
        )
    }

    override fun getItemCount(): Int = levelsAndLessons.groupBy { it.level }.size
}

class LessonsAdapter(
    private val levelsAndLesson: List<LevelAndLesson>,
    private val lessonItemClickEvent: ((Int, Int) -> Unit),
    private val userProgressInLesson: List<Int>,
    private val chosenLevelNum: Int
) : RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(
        private val lessonBinding: ItemLessonInLevelBinding
    ) : RecyclerView.ViewHolder(lessonBinding.root) {
        fun bind(number: Int, levelsAndLesson: List<LevelAndLesson>) {
            lessonBinding.apply {
                root.isClickable = userProgressInLesson.contains(number)
                lessonItem.setOnClickListener {
                    lessonItemClickEvent(chosenLevelNum, number)
                }

                if (userProgressInLesson.contains(number)) {
                    lessonNumber.text = "✓"
                    lessonProgress.progress = 100
                } else {
                    lessonNumber.text = number.toString()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder =
        LessonViewHolder(
            ItemLessonInLevelBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_lesson_in_level, parent, false)
            )
        )

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        // Получаем позицию каждого элемента, по ней находим урок
        holder.bind(number = position + 1, levelsAndLesson = levelsAndLesson.groupBy { it.lesson }.getOrDefault(position + 1, emptyList()))
    }

    override fun getItemCount(): Int = levelsAndLesson.groupBy { it.lesson }.size
}

