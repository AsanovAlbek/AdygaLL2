package com.example.adygall2.data.models

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Environment
import android.util.Log
import com.example.adygall2.data.db_models.Sound
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Класс созданный для управления проигрыванием аудиозаписей
 * Обёртка над классом MediaPlayer
 */

class SoundsPlayer(
    private val context : Context
) {

    // Экземпляр класса для работы с аудиофайлами
    val mediaPlayer = MediaPlayer()
    // Переменная для регулирования скорости воспроизведения
    var playbackSpeed = NORMAL_PLAYBACK
        // Константы
        companion object {
            // Медленное воспроизведение
            const val SLOW_PLAYBACK = 0.6f
            // Обычное воспроизведение
            const val NORMAL_PLAYBACK = 1f
    }

    /**
     * Метод для получения аудиофайла из базы данных и записи его в память телефона
     * @param sound - получаемый из бд экземпляр аудиозаписи
     */
    private fun getFileFromRoom(sound : Sound) : File {
        val audioFile = File(context.filesDir, "sound_${sound.id}")

        try {
            FileOutputStream(audioFile).use {
                it.write(sound.audioByteArray)
            }
        }
        catch (ex : FileNotFoundException) {
            Log.i("Sound", ex.message.toString())
        }

        return audioFile
    }

    /**
     * Метод для воспроизведения аудиофайла
     * @param sound - экземпляр класса Sound, получаемый из базы данных
     */
        fun playSound(sound : Sound) {
            val audioFile = getFileFromRoom(sound)
            if (audioFile.exists()) {
                val inputStream = FileInputStream(audioFile)
                mediaPlayer.apply {
                    setDataSource(inputStream.fd)
                    prepare()
                    setSpeed(playbackSpeed)
                    seekTo(0)
                    start()
                }
                inputStream.close()
            }
        }

    /**
     * Метод для остановки воспроизведения
     */
        fun stopPlay() {
                mediaPlayer.stop()
                mediaPlayer.reset()
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
