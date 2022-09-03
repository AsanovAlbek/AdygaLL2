package com.example.adygall2.data.db_models

/**
 * Модель для хранения аудиофайлов (озвучек)
 * @param id - id аудиофайла
 * @param name - название аудиофайла
 * @param audioByteArray - аудиофайл в байтовом представлении
 *
 * Так же, как и класс [Picture] переопределяет методы equals() и hashCode()
 */
data class Sound (
    val id : Int,
    val name : String,
    val audioByteArray : ByteArray
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sound

        if (id != other.id) return false
        if (name != other.name) return false
        if (!audioByteArray.contentEquals(other.audioByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + audioByteArray.contentHashCode()
        return result
    }
}