package com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem

import androidx.viewbinding.ViewBinding
import com.example.adygall2.data.room.consts.TaskType.FILL_IN_THE_GAPS
import com.example.adygall2.data.room.consts.TaskType.FILL_IN_THE_PASS
import com.example.adygall2.data.room.consts.TaskType.IMAGE
import com.example.adygall2.data.room.consts.TaskType.SELECT_PAIRS_OF_WORDS
import com.example.adygall2.data.room.consts.TaskType.SENTENCE_BUILD
import com.example.adygall2.data.room.consts.TaskType.THREE_WORDS
import com.example.adygall2.data.room.consts.TaskType.TRANSLATE_SENTENCE
import com.example.adygall2.data.room.consts.TaskType.TYPE_THAT_YOUR_HEARD
import com.example.adygall2.data.room.consts.TaskType.TYPE_TRANSLATE
import com.example.adygall2.data.room.consts.TaskType.UNKNOWN
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FillGapsQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FillPassQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FourImagesQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.PairQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.SentenceBuildQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.ThreeWordsQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TranslateTextQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TypeThanHeardQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TypeTranslateQuestionItem
import com.xwray.groupie.viewbinding.BindableItem

abstract class QuestionItem<T:ViewBinding>: BindableItem<T>() {
    open val userAnswer: String = ""
    open val rightAnswer: String = ""

    val taskType: Int
        get() = when(this) {
            is FourImagesQuestionItem -> IMAGE
            is SentenceBuildQuestionItem -> SENTENCE_BUILD
            is ThreeWordsQuestionItem -> THREE_WORDS
            is TypeTranslateQuestionItem -> TYPE_TRANSLATE
            is PairQuestionItem -> SELECT_PAIRS_OF_WORDS
            is TypeThanHeardQuestionItem -> TYPE_THAT_YOUR_HEARD
            is FillPassQuestionItem -> FILL_IN_THE_PASS
            is FillGapsQuestionItem -> FILL_IN_THE_GAPS
            is TranslateTextQuestionItem -> TRANSLATE_SENTENCE
            else -> UNKNOWN
        }

    abstract val onNextQuestion: () -> Unit

    val canSkipQuestion = when(taskType) {
        SENTENCE_BUILD, TYPE_THAT_YOUR_HEARD -> true
        else -> false
    }
}