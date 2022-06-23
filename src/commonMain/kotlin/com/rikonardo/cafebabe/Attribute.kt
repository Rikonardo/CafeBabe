package com.rikonardo.cafebabe

import com.rikonardo.cafebabe.data.AttributeData
import com.rikonardo.cafebabe.data.constantpool.ConstantUtf8

class Attribute(private val classFile: ClassFile, val data: AttributeData) {
    constructor(classFile: ClassFile, name: String, info: ByteArray) : this(
        classFile,
        AttributeData(classFile.constantPool.add(ConstantUtf8(name)), info)
    )

    var name: String
        get() {
            return (classFile.data.constantPool[data.attributeNameIndex] as ConstantUtf8).value
        }
        set(value) {
            (classFile.data.constantPool[data.attributeNameIndex] as ConstantUtf8).value = value
        }

    var info: ByteArray
        get() {
            return data.info
        }
        set(value) {
            data.info = value
        }

    override fun toString(): String {
        return "Attribute(name='${name}', info=ByteArray(size=${info.size}))"
    }
}
