package com.example.adygall2.data.models

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import com.example.adygall2.domain.model.Source
import java.io.FileInputStream

/**
 * Класс созданный для управления проигрыванием аудиозаписей из
 * Экземпляра класса [Source]
 * Обёртка над классом MediaPlayer
 */

class SoundsPlayer(
    private val context : Context,
    private val filesHandler: FilesHandler,
    private val mediaPlayer: MediaPlayer) {

    init {
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
    }

    /** Переменная для регулирования скорости воспроизведения */
    private var playbackSpeed = NORMAL_PLAYBACK
    /** Информация о проигрывании на данный момент
      * true - звук воспроизводится в данный момент,
       * false - звук не воспроизводится в данный момент*/
    val isPlayingNow get() = mediaPlayer.isPlaying
        // Константы
        companion object {
            /** Медленное воспроизведение */
            private const val SLOW_PLAYBACK = 0.6f
            /** Обычное воспроизведение */
            private const val NORMAL_PLAYBACK = 1f
    }

    /**
     * Метод для воспроизведения аудиофайла
     * @param source - экземпляр класса Sound, получаемый из базы данных
     */
        fun playSound(source : Source) {
            filesHandler.addOnSuccessListener(source) { fileDescriptor ->
                mediaPlayer.apply {
                    if (isPlayingNow) {
                        stopPlay()
                    } else {
                        setDataSource(fileDescriptor)
                        prepareAsync()
                        setSpeed(playbackSpeed)
                        //seekTo(0)
                    }
                }
            }
        }

    /**
     * Метод для остановки воспроизведения
     */
        fun stopPlay() {
            mediaPlayer.pause()
            mediaPlayer.reset()
        }

    /**
     * Метод для слушателя завершения проигрывания аудиозаписи
     */
    fun setCompletionListener(completionListener : ((MediaPlayer) -> Unit)) {
            mediaPlayer.setOnCompletionListener(completionListener)
    }

    fun reset() {
        mediaPlayer.reset()
    }

    fun slowPlaybackSpeed() {
        playbackSpeed = SLOW_PLAYBACK
    }

    fun normalPlaybackSpeed() {
        playbackSpeed = NORMAL_PLAYBACK
    }

    /**
     * Метод для установки скорости
     */
        private fun setSpeed(speed : Float) {
            val params = PlaybackParams()
            params.speed = speed
            mediaPlayer.playbackParams = params
        }
    }
