package com.rikonardo.cafebabe.utils

class Reader(private val binary: ByteArray) {
    private var index = 0

    fun readU1(): Int {
        return binary[index++].toInt() and 0xFF
    }

    fun readU2(): Int {
        val value = (binary[index++].toInt() and 0xFF shl 8) or (binary[index++].toInt() and 0xFF)
        return if (value and 0x8000 == 0x8000) value - 0x10000 else value
    }

    fun readU4(): Int {
        val value =
            (binary[index++].toInt() and 0xFF shl 24) or (binary[index++].toInt() and 0xFF shl 16) or (binary[index++].toInt() and 0xFF shl 8) or (binary[index++].toInt() and 0xFF)
        return if (value and 0x80000000.toInt() == 0x80000000.toInt()) value - 0x100000000.toInt() else value
    }

    fun readB4(): ByteArray {
        index += 4
        return binary.copyOfRange(index - 4, index)
    }

    fun readB8(): ByteArray {
        index += 8
        return binary.copyOfRange(index - 8, index)
    }

    fun readU1Array(count: Int): ByteArray {
        index += count
        return binary.copyOfRange(index - count, index)
    }
}
