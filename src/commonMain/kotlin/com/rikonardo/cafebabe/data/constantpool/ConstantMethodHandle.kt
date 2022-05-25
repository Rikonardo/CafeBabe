package com.rikonardo.cafebabe.data.constantpool

data class ConstantMethodHandle(
    var referenceKind: Int,
    var referenceIndex: Int,
) : AbstractConstant(15)
