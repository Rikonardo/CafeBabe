package com.rikonardo.cafebabe.data.constantpool

data class ConstantNameAndType(
    var nameIndex: Int,
    var descriptorIndex: Int,
) : AbstractConstant(12)
