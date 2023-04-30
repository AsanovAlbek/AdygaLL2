package com.example.adygall2.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.data.models.settings.ProgressItem
import com.example.adygall2.domain.model.Task
import com.example.adygall2.databinding.ItemLessonInLevelBinding
import com.example.adygall2.databinding.ItemLevelBinding
import com.google.android.material.button.MaterialButton

/**
 * Адаптер уровней, в котором содержится адаптер уроков
 * */
class LevelsAdapter(
    private val tasks : List<Task>,
    private val lessonClickEvent : ((Int,Int, List<Task>, String) -> Unit),
    private val userProgress: Set<ProgressItem>
) : RecyclerView.Adapter<LevelsAdapter.LevelHolder>() {

    private lateinit var lessonsAdapter: LessonsAdapter
    private val levelsNames = listOf(
        "Основы", "Выражения", "Семья", "Знакомства", "Еда", "Фрукты",
        "Овощи", "Животные", "Прилагательные", "Множественное число",
        "Одежда", "Цвета", "Природа", "Птицы", "Дом (интерьер)",
        "Предметы", "Части тела", "Местоимение", "Числительные",
        "Дата и время", "Профессии", "Глагол (настоящее время)",
        "Чувства. Эмоции", "Глагол (будущее время)", "Глагол (прошедшее время)",
        "Отрицательная форма", "Вопросительные предложения", "Повелительная  форма  глагола",
        "Наречие", "Республика Адыгея"
    )

    inner class LevelHolder(
        private val itemBinding: ItemLevelBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int, levelTitle: String, taskList: List<Task>) {
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

                val availableLessons = userProgress.filter { it.level == position + 1 }.map { it.lesson }
                itemBinding.root.isEnabled = userProgress.map { it.level }.contains(position)

                // Присваиваем адаптер и подаём ему список заданий
                lessonsAdapter = LessonsAdapter(
                    tasksInLevel = taskList,
                    lessonItemClickEvent = lessonClickEvent,
                    userProgressInLesson = availableLessons,
                    chosenLevelNum = position + 1,
                    lessonName = levelsNames[position]
                )
                lessonsList.adapter = lessonsAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelHolder =
        LevelHolder(
            ItemLevelBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
            )
        )

    override fun onBindViewHolder(holder: LevelHolder, position: Int) {
        holder.bind(position, levelTitle = levelsNames[position], taskList = tasks)
    }

    override fun getItemCount(): Int = levelsNames.size
}

// Адаптер подменю
class LessonsAdapter(
    private val tasksInLevel: List<Task>,
    private val lessonItemClickEvent: ((Int, Int, List<Task>, String) -> Unit),
    private val userProgressInLesson: List<Int>,
    private val chosenLevelNum: Int,
    private val lessonName: String
): RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(
        private val lessonBinding: ItemLessonInLevelBinding
    ): RecyclerView.ViewHolder(lessonBinding.root) {
        fun bind(number: Int, lessonTasks: List<Task>) {
            lessonBinding.apply {
                root.isClickable = userProgressInLesson.contains(number)
                lessonItem.setOnClickListener {
                    lessonItemClickEvent(chosenLevelNum, number, lessonTasks, lessonName)
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
        holder.bind(number = position + 1, lessonTasks = tasksInLevel.chunked(15)[position])
    }

    override fun getItemCount(): Int = 3
}