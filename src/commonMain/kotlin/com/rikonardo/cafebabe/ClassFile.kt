package com.rikonardo.cafebabe

import com.rikonardo.cafebabe.data.*
import com.rikonardo.cafebabe.data.constantpool.ConstantClass
import com.rikonardo.cafebabe.data.constantpool.ConstantUtf8
import com.rikonardo.cafebabe.utils.ObservableList
import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

class ClassFile(binary: ByteArray) {
    val data: ClassFileData

    init {
        val reader = Reader(binary)
        val magic = reader.readU4()
        val minorVersion = reader.readU2()
        val majorVersion = reader.readU2()
        val constantPoolCount = reader.readU2()
        val constantPool = ConstantPool.from(reader, constantPoolCount)
        val accessFlags = reader.readU2()
        val thisClass = reader.readU2()
        val superClass = reader.readU2()
        val interfacesCount = reader.readU2()
        val interfaces = InterfaceData.from(reader, interfacesCount)
        val fieldsCount = reader.readU2()
        val fields = FieldData.from(reader, fieldsCount)
        val methodsCount = reader.readU2()
        val methods = MethodData.from(reader, methodsCount)
        val attributesCount = reader.readU2()
        val attributes = AttributeData.from(reader, attributesCount)
        data = ClassFileData(
            magic,
            minorVersion,
            majorVersion,
            constantPool,
            accessFlags,
            thisClass,
            superClass,
            interfaces,
            fields,
            methods,
            attributes
        )
    }

    fun compile(): ByteArray {
        val writer = Writer()
        writer.writeU4(data.magic)
        writer.writeU2(data.minorVersion)
        writer.writeU2(data.majorVersion)
        writer.writeU2(data.constantPool.entries.size + 1)
        ConstantPool.to(writer, data.constantPool)
        writer.writeU2(data.accessFlags)
        writer.writeU2(data.thisClass)
        writer.writeU2(data.superClass)
        writer.writeU2(data.interfaces.size)
        InterfaceData.to(writer, data.interfaces)
        writer.writeU2(data.fields.size)
        FieldData.to(writer, data.fields)
        writer.writeU2(data.methods.size)
        MethodData.to(writer, data.methods)
        writer.writeU2(data.attributes.size)
        AttributeData.to(writer, data.attributes)
        return writer.toByteArray()
    }

    var magic: Int
        get() = data.magic
        set(value) {
            data.magic = value
        }

    val version = ClassVersion(this)

    val constantPool: ConstantPool
        get() = data.constantPool

    var name: String
        get() {
            val classConst = constantPool[data.thisClass] as ConstantClass
            return (data.constantPool[classConst.nameIndex] as ConstantUtf8).value
        }
        set(value) {
            val classConst = constantPool[data.thisClass] as ConstantClass
            (constantPool[classConst.nameIndex] as ConstantUtf8).value = value
        }

    var parent: String
        get() {
            val classConst = constantPool[data.superClass] as ConstantClass
            return (data.constantPool[classConst.nameIndex] as ConstantUtf8).value
        }
        set(value) {
            val classConst = constantPool[data.superClass] as ConstantClass
            (constantPool[classConst.nameIndex] as ConstantUtf8).value = value
        }

    var access: MutableList<AccessFlag>
        get() = ObservableList(AccessFlag.from(data.accessFlags))
            .apply { addListener { data.accessFlags = AccessFlag.to(it) } }
        set(value) {
            data.accessFlags = AccessFlag.to(value)
        }

    var interfaces: MutableList<Interface>
        get() = ObservableList(data.interfaces.map { Interface(this, it) })
            .apply { addListener { data.interfaces = this.map { it.data } } }
        set(value) {
            data.interfaces = value.map { it.data }
        }

    var fields: MutableList<Field>
        get() = ObservableList(data.fields.map { Field(this, it) })
            .apply { addListener { data.fields = this.map { it.data } } }
        set(value) {
            data.fields = value.map { it.data }
        }

    var methods: MutableList<Method>
        get() = ObservableList(data.methods.map { Method(this, it) })
            .apply { addListener { data.methods = this.map { it.data } } }
        set(value) {
            data.methods = value.map { it.data }
        }

    var attributes: MutableList<Attribute>
        get() = ObservableList(data.attributes.map { Attribute(this, it) })
            .apply { addListener { data.attributes = this.map { it.data } } }
        set(value) {
            data.attributes = value.map { it.data }
        }

    override fun toString(): String {
        return "ClassFile(magic=0x${
            magic.toUInt().toString(16).uppercase()
        }, version=$version, name=$name, parent=$parent, access=${
            access.joinToString(" ") { it.name.lowercase() }
        }, interfaces=$interfaces, fields=$fields, methods=$methods, attributes=$attributes)"
    }
}
