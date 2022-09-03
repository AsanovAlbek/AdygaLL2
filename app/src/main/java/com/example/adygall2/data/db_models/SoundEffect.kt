package com.example.adygall2.data.db_models

/**
 * Модель для звукщвых эффектов при ответе на задание
 * @param id - id эффекта
 * @param name - название эффекта
 * @param effect - звуковой эффект в массиве байтов
 */
data class SoundEffect(
    val id : Int,
    val name : String,
    val effect : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffect

        if (id != other.id) return false
        if (name != other.name) return false
        if (!effect.contentEquals(other.effect)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + effect.contentHashCode()
        return result
    }
}
