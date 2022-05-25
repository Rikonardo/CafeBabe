package com.rikonardo.cafebabe.data.numbers

import kotlin.jvm.JvmInline

@JvmInline
value class BinaryDouble(val value: Double) {
    companion object {
        fun from(byteArray: ByteArray): BinaryDouble {
            return BinaryDouble(Double.fromBits(BinaryLong.from(byteArray).value))
        }
    }

    fun toByteArray(): ByteArray {
        return BinaryLong(value.toBits()).toByteArray()
    }
}
