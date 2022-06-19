package com.example.adygall2.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.data.room.consts.TaskType.IMAGE_TASK
import com.example.adygall2.data.room.consts.TaskType.PHRASE_COMPLETE_TASK
import com.example.adygall2.data.room.consts.TaskType.SENTENCE_BUILDER_TASK
import com.example.adygall2.data.room.consts.TaskType.THREE_WORDS_TASK
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TYPE_KEY
import com.example.adygall2.presentation.fragments.*
import java.lang.ref.WeakReference

class TasksAdapter(
    private val fragmentActivity: FragmentActivity,
    private val tasks : List<Task>
) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentMap = mutableMapOf<Int, WeakReference<Fragment>>()

    override fun getItemCount() = tasks.size

    val tasksCount get() = tasks.size

    fun getTaskFragment(position: Int) : Fragment? = fragmentMap[position]?.get()

    override fun createFragment(position: Int): Fragment {
        val task = tasks[position]
        val taskFragment = buildTask(task.taskType)

        fragmentMap[position] = WeakReference(taskFragment)

        val bundle = Bundle()
        with(bundle) {
            putInt(ID_KEY, task.id)
            putInt(TYPE_KEY, task.taskType)
            putString(TASK_KEY, task.task)
            putString("userAnswer", "")
            putString("rightAnswer", "")
        }

        taskFragment.arguments = bundle

        return taskFragment
    }

    private fun buildTask(taskType : Int) : Fragment = when(taskType) {

        IMAGE_TASK -> FourImageQuestion()

        SENTENCE_BUILDER_TASK -> SentenceBuildQuestion()

        THREE_WORDS_TASK -> ThreeWordsQuestion()

        PHRASE_COMPLETE_TASK -> PhraseFragment()

        else -> Fragment()
    }
}