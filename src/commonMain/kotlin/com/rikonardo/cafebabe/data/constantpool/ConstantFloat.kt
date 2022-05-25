package com.rikonardo.cafebabe.data.constantpool

import com.rikonardo.cafebabe.data.numbers.*

data class ConstantFloat(
    var value: BinaryFloat,
) : AbstractConstant(4)
