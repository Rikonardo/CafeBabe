package com.rikonardo.cafebabe.data.numbers

import kotlin.jvm.JvmInline

@JvmInline
value class BinaryInt(val value: Int) {
    companion object {
        fun from(byteArray: ByteArray): BinaryInt {
            var result = 0
            for (i in byteArray.indices) {
                result = result or (byteArray[byteArray.lastIndex - i].toInt() and 0xFF shl (i * 8))
            }
            return BinaryInt(result)
        }
    }

    fun toByteArray(): ByteArray {
        return byteArrayOf(
            (value shr 24).toByte(), (value shr 16).toByte(), (value shr 8).toByte(), value.toByte()
        )
    }
}
