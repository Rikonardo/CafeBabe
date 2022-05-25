package com.rikonardo.cafebabe.data

import com.rikonardo.cafebabe.data.constantpool.*
import com.rikonardo.cafebabe.data.numbers.*
import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

data class ConstantPool(
    var entries: MutableList<AbstractConstant> = mutableListOf()
) {
    fun add(entry: AbstractConstant): Int {
        entries.add(entry)
        return entries.size
    }

    operator fun get(index: Int): AbstractConstant {
        return entries[index - 1]
    }

    val size: Int
        get() = entries.size

    enum class ConstantType(val tag: Int) {
        Class(7),
        Fieldref(9),
        Methodref(10),
        InterfaceMethodref(11),
        String(8),
        Integer(3),
        Float(4),
        Long(5),
        Double(6),
        NameAndType(12),
        Utf8(1),
        MethodHandle(15),
        MethodType(16),
        InvokeDynamic(18),
        Module(19),
        Package(20),
    }

    companion object {
        fun from(reader: Reader, count: Int): ConstantPool {
            val constantPool = ConstantPool()
            for (i in 1 until count) {
                val tag = reader.readU1()
                when (tag) {
                    ConstantType.Class.tag -> {
                        val nameIndex = reader.readU2()
                        constantPool.add(ConstantClass(nameIndex))
                    }
                    ConstantType.Fieldref.tag -> {
                        val classIndex = reader.readU2()
                        val nameAndTypeIndex = reader.readU2()
                        constantPool.add(ConstantFieldref(classIndex, nameAndTypeIndex))
                    }
                    ConstantType.Methodref.tag -> {
                        val classIndex = reader.readU2()
                        val nameAndTypeIndex = reader.readU2()
                        constantPool.add(ConstantMethodref(classIndex, nameAndTypeIndex))
                    }
                    ConstantType.InterfaceMethodref.tag -> {
                        val classIndex = reader.readU2()
                        val nameAndTypeIndex = reader.readU2()
                        constantPool.add(ConstantInterfaceMethodref(classIndex, nameAndTypeIndex))
                    }
                    ConstantType.String.tag -> {
                        val stringIndex = reader.readU2()
                        constantPool.add(ConstantString(stringIndex))
                    }
                    ConstantType.Integer.tag -> {
                        val bytes = reader.readB4()
                        constantPool.add(ConstantInteger(BinaryInt.from(bytes)))
                    }
                    ConstantType.Float.tag -> {
                        val bytes = reader.readB4()
                        constantPool.add(ConstantFloat(BinaryFloat.from(bytes)))
                    }
                    ConstantType.Long.tag -> {
                        val bytes = reader.readB8()
                        constantPool.add(ConstantLong(BinaryLong.from(bytes)))
                    }
                    ConstantType.Double.tag -> {
                        val bytes = reader.readB8()
                        constantPool.add(ConstantDouble(BinaryDouble.from(bytes)))
                    }
                    ConstantType.NameAndType.tag -> {
                        val nameIndex = reader.readU2()
                        val descriptorIndex = reader.readU2()
                        constantPool.add(ConstantNameAndType(nameIndex, descriptorIndex))
                    }
                    ConstantType.Utf8.tag -> {
                        val length = reader.readU2()
                        val bytes = reader.readU1Array(length)
                        constantPool.add(ConstantUtf8(bytes.decodeToString()))
                    }
                    ConstantType.MethodHandle.tag -> {
                        val referenceKind = reader.readU1()
                        val referenceIndex = reader.readU2()
                        constantPool.add(ConstantMethodHandle(referenceKind, referenceIndex))
                    }
                    ConstantType.MethodType.tag -> {
                        val descriptorIndex = reader.readU2()
                        constantPool.add(ConstantMethodType(descriptorIndex))
                    }
                    ConstantType.InvokeDynamic.tag -> {
                        val bootstrapMethodAttrIndex = reader.readU2()
                        val nameAndTypeIndex = reader.readU2()
                        constantPool.add(ConstantInvokeDynamic(bootstrapMethodAttrIndex, nameAndTypeIndex))
                    }
                    ConstantType.Module.tag -> {
                        val nameIndex = reader.readU2()
                        constantPool.add(ConstantModule(nameIndex))
                    }
                    ConstantType.Package.tag -> {
                        val nameIndex = reader.readU2()
                        constantPool.add(ConstantPackage(nameIndex))
                    }
                    else -> throw IllegalArgumentException("Unknown constant type: $tag")
                }
            }
            return constantPool
        }

        fun to(writer: Writer, constantPool: ConstantPool) {
            for (entry in constantPool.entries) {
                when (entry) {
                    is ConstantClass -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.nameIndex)
                    }
                    is ConstantFieldref -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.classIndex)
                        writer.writeU2(entry.nameAndTypeIndex)
                    }
                    is ConstantMethodref -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.classIndex)
                        writer.writeU2(entry.nameAndTypeIndex)
                    }
                    is ConstantInterfaceMethodref -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.classIndex)
                        writer.writeU2(entry.nameAndTypeIndex)
                    }
                    is ConstantString -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.stringIndex)
                    }
                    is ConstantInteger -> {
                        writer.writeU1(entry.tag)
                        writer.writeU1Array(entry.value.toByteArray())
                    }
                    is ConstantFloat -> {
                        writer.writeU1(entry.tag)
                        writer.writeU1Array(entry.value.toByteArray())
                    }
                    is ConstantLong -> {
                        writer.writeU1(entry.tag)
                        writer.writeU1Array(entry.value.toByteArray())
                    }
                    is ConstantDouble -> {
                        writer.writeU1(entry.tag)
                        writer.writeU1Array(entry.value.toByteArray())
                    }
                    is ConstantNameAndType -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.nameIndex)
                        writer.writeU2(entry.descriptorIndex)
                    }
                    is ConstantUtf8 -> {
                        writer.writeU1(entry.tag)
                        val bytes = entry.value.encodeToByteArray()
                        writer.writeU2(bytes.size)
                        writer.writeU1Array(bytes)
                    }
                    is ConstantMethodHandle -> {
                        writer.writeU1(entry.tag)
                        writer.writeU1(entry.referenceKind)
                        writer.writeU2(entry.referenceIndex)
                    }
                    is ConstantMethodType -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.descriptorIndex)
                    }
                    is ConstantInvokeDynamic -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.bootstrapMethodAttrIndex)
                        writer.writeU2(entry.nameAndTypeIndex)
                    }
                    is ConstantModule -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.nameIndex)
                    }
                    is ConstantPackage -> {
                        writer.writeU1(entry.tag)
                        writer.writeU2(entry.nameIndex)
                    }
                    else -> throw IllegalArgumentException("Unknown constant type: ${entry.tag}")
                }
            }
        }
    }
}
