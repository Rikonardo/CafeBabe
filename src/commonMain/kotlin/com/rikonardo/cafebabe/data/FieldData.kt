package com.rikonardo.cafebabe.data

import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

data class FieldData(
    var accessFlags: Int,
    var nameIndex: Int,
    var descriptorIndex: Int,
    var attributes: List<AttributeData>
) {
    companion object {
        fun from(reader: Reader, count: Int): MutableList<FieldData> {
            val fields = mutableListOf<FieldData>()
            for (i in 0 until count) {
                val accessFlags = reader.readU2()
                val nameIndex = reader.readU2()
                val descriptorIndex = reader.readU2()
                val attributesCount = reader.readU2()
                val attributes = AttributeData.from(reader, attributesCount)
                fields.add(FieldData(accessFlags, nameIndex, descriptorIndex, attributes))
            }
            return fields
        }

        fun to(writer: Writer, fields: List<FieldData>) {
            for (field in fields) {
                writer.writeU2(field.accessFlags)
                writer.writeU2(field.nameIndex)
                writer.writeU2(field.descriptorIndex)
                writer.writeU2(field.attributes.size)
                AttributeData.to(writer, field.attributes)
            }
        }
    }
}
