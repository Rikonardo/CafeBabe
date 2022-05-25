package com.rikonardo.cafebabe.data.numbers

import kotlin.jvm.JvmInline

@JvmInline
value class BinaryFloat(val value: Float) {
    companion object {
        fun from(byteArray: ByteArray): BinaryFloat {
            return BinaryFloat(Float.fromBits(BinaryInt.from(byteArray).value))
        }
    }

    fun toByteArray(): ByteArray {
        return BinaryInt(value.toBits()).toByteArray()
    }
}
