package com.rikonardo.cafebabe.data.constantpool

data class ConstantFieldref(
    var classIndex: Int,
    var nameAndTypeIndex: Int,
) : AbstractConstant(9)