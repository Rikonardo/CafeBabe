package com.rikonardo.cafebabe.utils

class Writer {
    var data = ByteArray(1024)
    var filled = 0
    fun writeU1Array(byteArray: ByteArray) {
        if (data.size - filled < byteArray.size) {
            val newData = ByteArray(data.size * 2)
            data.copyInto(newData)
            data = newData
        }
        byteArray.copyInto(data, filled)
        filled += byteArray.size
    }

    fun writeU1(value: Int) {
        writeU1Array(byteArrayOf(value.toByte()))
    }

    fun writeU2(value: Int) {
        writeU1Array(byteArrayOf((value shr 8).toByte(), (value and 0xFF).toByte()))
    }

    fun writeU4(value: Int) {
        writeU1Array(
            byteArrayOf(
                (value shr 24).toByte(),
                (value shr 16).toByte(),
                (value shr 8).toByte(),
                (value and 0xFF).toByte()
            )
        )
    }

    fun toByteArray(): ByteArray {
        val result = ByteArray(filled)
        data.copyInto(result, 0, 0, filled)
        return result
    }
}
