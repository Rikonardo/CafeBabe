package com.rikonardo.cafebabe.data.constantpool

data class ConstantInterfaceMethodref(
    var classIndex: Int,
    var nameAndTypeIndex: Int,
) : AbstractConstant(11)
