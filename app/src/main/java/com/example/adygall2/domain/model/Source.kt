package com.example.adygall2.domain.model

data class Source (
    val id : Int,
    val name : String,
    val source : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Source

        if (id != other.id) return false
        if (name != other.name) return false
        if (!source.contentEquals(other.source)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + source.contentHashCode()
        return result
    }
}