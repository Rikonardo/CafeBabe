package com.rikonardo.cafebabe.data.numbers

import kotlin.jvm.JvmInline

@JvmInline
value class BinaryLong(val value: Long) {
    companion object {
        fun from(byteArray: ByteArray): BinaryLong {
            var result = 0L
            for (i in byteArray.indices) {
                result = result or (byteArray[byteArray.lastIndex - i].toLong() and 0xFF shl (i * 8))
            }
            return BinaryLong(result)
        }
    }

    fun toByteArray(): ByteArray {
        return byteArrayOf(
            (value shr 56).toByte(),
            (value shr 48).toByte(),
            (value shr 40).toByte(),
            (value shr 32).toByte(),
            (value shr 24).toByte(),
            (value shr 16).toByte(),
            (value shr 8).toByte(),
            value.toByte()
        )
    }
}
