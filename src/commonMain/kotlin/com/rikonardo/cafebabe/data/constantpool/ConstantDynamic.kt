package com.rikonardo.cafebabe.data.constantpool

data class ConstantDynamic(
    var bootstrapMethodAttrIndex: Int,
    var nameAndTypeIndex: Int,
) : AbstractConstant(17)
