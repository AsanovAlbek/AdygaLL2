package com.example.adygall2.presentation.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.RotateDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.adygall2.R
import com.example.adygall2.domain.model.Task
import com.example.adygall2.databinding.ItemLessonInLevelBinding
import com.example.adygall2.databinding.ItemLevelBinding
import com.example.adygall2.domain.lesson
import com.example.adygall2.domain.level
import com.example.adygall2.domain.levels

/**
 * Адаптер уровней, в котором содержится адаптер уроков
 * */
class LevelsAdapter(
    private val context: Context,
    private val tasks : List<Task>,
    private val lessonClickEvent : ((Int,Int, List<Task>) -> Unit),
    private val completedLesson: Int,
    private val completedLevel: Int
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

                // Присваиваем адаптер и подаём ему список заданий
                lessonsAdapter = LessonsAdapter(
                    tasksInLevel = taskList,
                    lessonItemClickEvent = lessonClickEvent,
                    completedLevel = completedLevel,
                    completedLesson = completedLesson,
                    chosenLevelNum = position + 1
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
        //holder.bind(taskList = tasks.levels().level(position + 1))
        holder.bind(position, levelTitle = levelsNames[position], taskList = tasks)
    }

    //override fun getItemCount(): Int = tasks.levels().size
    override fun getItemCount(): Int = levelsNames.size
}

// Адаптер подменю
class LessonsAdapter(
    private val tasksInLevel: List<Task>,
    private val lessonItemClickEvent: ((Int, Int, List<Task>) -> Unit),
    private val completedLevel: Int,
    private val completedLesson: Int,
    private val chosenLevelNum: Int
): RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(
        private val lessonBinding: ItemLessonInLevelBinding
    ): RecyclerView.ViewHolder(lessonBinding.root) {
        fun bind(number: Int, lessonTasks: List<Task>) {

            lessonBinding.apply {
                lessonItem.isClickable = completedLevel <= number
                lessonItem.setOnClickListener {
                    Log.d("lessonAdapter","complLvl: $completedLevel, compLesson: $completedLesson, curLesson: $number, curLvl: $chosenLevelNum")
                    lessonItemClickEvent(chosenLevelNum, number, lessonTasks)
                }
                if (completedLevel <= chosenLevelNum) {
                    if (completedLesson < number) {
                        lessonNumber.text = number.toString()
                    } else {
                        lessonNumber.text = "✓"
                        lessonProgress.progress = 100
                    }
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

    override fun getItemCount(): Int = 12
}