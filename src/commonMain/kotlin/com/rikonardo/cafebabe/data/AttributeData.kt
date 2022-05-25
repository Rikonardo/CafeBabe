package com.rikonardo.cafebabe.data

import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

@Suppress("ArrayInDataClass")
data class AttributeData(
    var attributeNameIndex: Int,
    var info: ByteArray
) {
    companion object {
        fun from(reader: Reader, count: Int): MutableList<AttributeData> {
            val attributes = mutableListOf<AttributeData>()
            for (i in 0 until count) {
                val attributeNameIndex = reader.readU2()
                val attributeLength = reader.readU4()
                attributes.add(AttributeData(attributeNameIndex, reader.readU1Array(attributeLength)))
            }
            return attributes
        }

        fun to(writer: Writer, attributes: List<AttributeData>) {
            for (attribute in attributes) {
                writer.writeU2(attribute.attributeNameIndex)
                writer.writeU4(attribute.info.size)
                writer.writeU1Array(attribute.info)
            }
        }
    }
}
