package com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.room.consts.TaskType
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FillGapsQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FillPassQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.FourImagesQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.PairQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.SentenceBuildQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.ThreeWordsQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TranslateTextQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TypeThanHeardQuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.childs.TypeTranslateQuestionItem

fun Task.createQuestion(
    context: Context,
    title: String,
    answers: List<ComplexAnswer>,
    soundsPlayer: SoundsPlayer,
    onClearImageCaches: () -> Unit,
    playerSource: Source
): QuestionItem<out ViewBinding>? = when(taskType) {
    TaskType.IMAGE -> FourImagesQuestionItem(
        context = context,
        title = title,
        answers = answers,
        soundsPlayer = soundsPlayer,
        onClearImages = onClearImageCaches
    )

    TaskType.THREE_WORDS -> ThreeWordsQuestionItem(
        context = context,
        title = title,
        answers = answers
    )

    TaskType.TYPE_TRANSLATE -> TypeTranslateQuestionItem(
        context = context,
        title = title,
        currentAnswer = answers.first().answer.correctAnswer
    )

    TaskType.SENTENCE_BUILD -> SentenceBuildQuestionItem(
        context = context,
        title = title,
        answers = answers,
        soundsPlayer = soundsPlayer,
        playerSource = playerSource
    )

    TaskType.FILL_IN_THE_GAPS -> FillGapsQuestionItem(
        context = context,
        title = title,
        answers = answers
    )

    TaskType.FILL_IN_THE_PASS -> FillPassQuestionItem(
        context = context,
        title = title,
        answers = answers
    )

    TaskType.TYPE_THAT_YOUR_HEARD -> TypeThanHeardQuestionItem(
        context = context,
        title = title,
        answers = answers,
        soundsPlayer = soundsPlayer,
        playerSource = playerSource
    )

    TaskType.TRANSLATE_SENTENCE -> TranslateTextQuestionItem(
        context = context,
        title = title,
        answers = answers
    )

    TaskType.SELECT_PAIRS_OF_WORDS -> PairQuestionItem(
        context = context,
        title = title,
        answers = answers
    )

    else -> null
}