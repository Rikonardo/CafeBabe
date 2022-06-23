package com.rikonardo.cafebabe

import com.rikonardo.cafebabe.data.MethodData
import com.rikonardo.cafebabe.data.constantpool.ConstantUtf8
import com.rikonardo.cafebabe.utils.ObservableList

class Method(private val classFile: ClassFile, val data: MethodData) {
    constructor(
        classFile: ClassFile,
        name: String,
        descriptor: String = "()V",
        accessFlags: List<AccessFlag> = listOf(),
        attributes: List<Attribute> = listOf()
    ) : this(
        classFile,
        MethodData(
            AccessFlag.to(accessFlags),
            classFile.constantPool.add(ConstantUtf8(name)),
            classFile.constantPool.add(ConstantUtf8(descriptor)),
            attributes.map { it.data }
        )
    )

    var name: String
        get() = (classFile.data.constantPool[data.nameIndex] as ConstantUtf8).value
        set(value) {
            (classFile.data.constantPool[data.nameIndex] as ConstantUtf8).value = value
        }

    var access: List<AccessFlag>
        get() = ObservableList(AccessFlag.from(data.accessFlags))
            .apply { addListener { data.accessFlags = AccessFlag.to(it) } }
        set(value) {
            data.accessFlags = AccessFlag.to(value)
        }

    var descriptor: String
        get() = (classFile.data.constantPool[data.descriptorIndex] as ConstantUtf8).value
        set(value) {
            (classFile.data.constantPool[data.descriptorIndex] as ConstantUtf8).value = value
        }

    var attributes: MutableList<Attribute>
        get() = ObservableList(data.attributes.map { Attribute(classFile, it) })
            .apply { addListener { data.attributes = this.map { it.data } } }
        set(value) {
            data.attributes = value.map { it.data }
        }

    override fun toString(): String {
        return "${access.joinToString(" ") { it.name.lowercase() }} ${name}${descriptor}"
    }
}
