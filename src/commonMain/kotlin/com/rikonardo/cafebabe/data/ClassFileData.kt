package com.rikonardo.cafebabe.data

@Suppress("ArrayInDataClass")
data class ClassFileData(
    var magic: Int,
    var minorVersion: Int,
    var majorVersion: Int,
    var constantPool: ConstantPool,
    var accessFlags: Int,
    var thisClass: Int,
    var superClass: Int,
    var interfaces: List<InterfaceData>,
    var fields: List<FieldData>,
    var methods: List<MethodData>,
    var attributes: List<AttributeData>
)
