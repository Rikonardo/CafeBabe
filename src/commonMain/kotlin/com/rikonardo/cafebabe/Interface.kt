package com.rikonardo.cafebabe

import com.rikonardo.cafebabe.data.InterfaceData
import com.rikonardo.cafebabe.data.constantpool.ConstantClass
import com.rikonardo.cafebabe.data.constantpool.ConstantUtf8

class Interface(private val classFile: ClassFile, val data: InterfaceData) {
    var name: String
        get() {
            val classConst = classFile.data.constantPool[data.index] as ConstantClass
            return (classFile.data.constantPool[classConst.nameIndex] as ConstantUtf8).value
        }
        set(value) {
            val classConst = classFile.data.constantPool[data.index] as ConstantClass
            (classFile.data.constantPool[classConst.nameIndex] as ConstantUtf8).value = value
        }

    override fun toString(): String {
        return name
    }
}