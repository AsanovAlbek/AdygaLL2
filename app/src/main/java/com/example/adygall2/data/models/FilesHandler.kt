package com.example.adygall2.data.models

import android.content.Context
import android.util.Log
import com.example.adygall2.domain.model.Source
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

/** Класс для управления файлами в SoundsPlayer */
class FilesHandler(private val context : Context) {
    /**
     * Метод для получения аудиофайла из базы данных и записи его в память телефона (внутреннее хранилище)
     * @param source - получаемый из бд экземпляр аудиозаписи
     * @return файл, в который записанны данные из source.source
     */
    private fun getFileFromRoom(source : Source) : File {
        val audioFile = File(context.filesDir, "sound_${source.id}")

        try {
            FileOutputStream(audioFile).use {
                it.write(source.source)
            }
        }
        catch (ex : FileNotFoundException) {
            Log.i("Sound", ex.message.toString())
        }

        return audioFile
    }

    /** Слушатель события, когда файл найден и может быть воспроизведён
     *  @param source - экземпляр класса [Source], из которого извлечётся в файл озвучка
     *  @param listener - слушатель, принимающий в себя [FileDescriptor], необходимый для проигрывания озвучки
     */
    fun addOnSuccessListener(source: Source, listener : ((FileDescriptor) -> Unit)) {
        val audioFile = getFileFromRoom(source)
        if (audioFile.exists() && audioFile.readBytes().isNotEmpty()) {
            val inputStream = FileInputStream(audioFile)
            listener.invoke(inputStream.fd)
            inputStream.close()
        }
    }
}