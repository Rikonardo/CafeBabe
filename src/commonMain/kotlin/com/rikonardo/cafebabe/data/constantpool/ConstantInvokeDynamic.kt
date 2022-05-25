package com.rikonardo.cafebabe.data.constantpool

data class ConstantInvokeDynamic(
    var bootstrapMethodAttrIndex: Int,
    var nameAndTypeIndex: Int,
) : AbstractConstant(18)
