package com.example.adygall2.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adygall2.domain.model.Task
import com.example.adygall2.data.room.consts.TaskType.FILL_IN_THE_GAPS
import com.example.adygall2.data.room.consts.TaskType.FILL_IN_THE_PASS
import com.example.adygall2.data.room.consts.TaskType.IMAGE
import com.example.adygall2.data.room.consts.TaskType.SELECT_PAIRS_OF_WORDS
import com.example.adygall2.data.room.consts.TaskType.SENTENCE_BUILD
import com.example.adygall2.data.room.consts.TaskType.THREE_WORDS
import com.example.adygall2.data.room.consts.TaskType.TRANSLATE_SENTENCE
import com.example.adygall2.data.room.consts.TaskType.TYPE_THAT_YOUR_HEARD
import com.example.adygall2.data.room.consts.TaskType.TYPE_TRANSLATE
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TYPE_KEY
import com.example.adygall2.presentation.fragments.tasks.*
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import java.lang.ref.WeakReference
import org.koin.android.ext.android.get

/**
 * Адаптер для создания BaseTaskFragment и его потомков
 * А так же помещение их в ViewPager2
 *
 * @param tasks - лист заданий, по которым создаётся кастомный фрагмент
 * @param fragmentActivity - родительский фрагмент
 */
class TasksAdapter(
    private val fragmentActivity: FragmentActivity,
    private val tasks : List<Task>
) : FragmentStateAdapter(fragmentActivity) {

    /** Обратный вызов для пропуска задания */
    private var taskSkipEvent : (() -> Unit)? = null

    /** мапа для хранения фрагментов по их номеру */
    //private val fragmentMap = mutableMapOf<Int, WeakReference<BaseTaskFragment>>()
    private val fragmentMap = mutableMapOf<Int, BaseTaskFragment>()

    override fun getItemCount() = tasks.size

    /** метод для получения фрагмента по позиции */
    fun getTaskFragment(position: Int) : BaseTaskFragment? = fragmentMap[position]

    override fun createFragment(position: Int): BaseTaskFragment {
        val task = tasks[position]
        val taskFragment = buildTask(task.taskType)

        //fragmentMap[position] = WeakReference(taskFragment)
        fragmentMap[position] = taskFragment

        // При создании фрагмента ему нужно передать некоторые данные из задания
        val bundle = Bundle()
        with(bundle) {
            putInt(ID_KEY, task.id)
            putInt(TYPE_KEY, task.taskType)
            putString(TASK_KEY, task.task)
            putInt(SOUND_KEY, task.soundId)
        }

        taskFragment.arguments = bundle

        return taskFragment
    }

    private fun buildTask(taskType : Int) : BaseTaskFragment = when(taskType) {

        IMAGE -> FourImageFragment()

        SENTENCE_BUILD -> SentenceBuildFragment(taskSkipEvent!!)

        THREE_WORDS -> ThreeWordsFragment()

        TRANSLATE_SENTENCE -> TranslateTheTextFragment()

        SELECT_PAIRS_OF_WORDS -> PairsOfWordsFragment()

        TYPE_THAT_YOUR_HEARD -> TypeThatHeardFragment()

        FILL_IN_THE_PASS -> FillPassFragment()

        FILL_IN_THE_GAPS -> FillGapsFragment()

        TYPE_TRANSLATE -> TypeTranslateFragment()

        else -> BaseTaskFragment(0)
    }

    fun setTaskSkipListener(listener : (() -> Unit)) {
        taskSkipEvent = listener
    }

    fun onFinish() {
        fragmentMap.clear()
    }

}