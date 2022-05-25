package com.rikonardo.cafebabe.data

import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

data class MethodData(
    var accessFlags: Int,
    var nameIndex: Int,
    var descriptorIndex: Int,
    var attributes: List<AttributeData>
) {
    companion object {
        fun from(reader: Reader, count: Int): MutableList<MethodData> {
            val methods = mutableListOf<MethodData>()
            for (i in 0 until count) {
                val accessFlags = reader.readU2()
                val nameIndex = reader.readU2()
                val descriptorIndex = reader.readU2()
                val attributesCount = reader.readU2()
                val attributes = AttributeData.from(reader, attributesCount)
                methods.add(MethodData(accessFlags, nameIndex, descriptorIndex, attributes))
            }
            return methods
        }

        fun to(writer: Writer, methods: List<MethodData>) {
            for (method in methods) {
                writer.writeU2(method.accessFlags)
                writer.writeU2(method.nameIndex)
                writer.writeU2(method.descriptorIndex)
                writer.writeU2(method.attributes.size)
                AttributeData.to(writer, method.attributes)
            }
        }
    }
}
