package com.rikonardo.cafebabe.data.constantpool

data class ConstantMethodref(
    var classIndex: Int,
    var nameAndTypeIndex: Int,
) : AbstractConstant(10)
