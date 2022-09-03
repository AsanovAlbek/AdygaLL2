package com.example.adygall2.data.db_models

/**
 * Модель картинок ответов
 * @param id - id картинки
 * @param name - название картинки
 * @param picture - PNG картинка в байтовом представлении
 *
 * Так же класс переопределяет методы equals() и hashCode()
 * для корректного сравнения картинок
 */
data class Picture(
    val id : Int,

    val name : String,

    val picture : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Picture

        if (id != other.id) return false
        if (name != other.name) return false
        if (!picture.contentEquals(other.picture)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + picture.contentHashCode()
        return result
    }
}
