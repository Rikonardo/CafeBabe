package com.rikonardo.cafebabe.data

import com.rikonardo.cafebabe.utils.Reader
import com.rikonardo.cafebabe.utils.Writer

data class InterfaceData(
    var index: Int
) {
    companion object {
        fun from(reader: Reader, count: Int): MutableList<InterfaceData> {
            val interfaces = mutableListOf<InterfaceData>()
            for (i in 0 until count) {
                interfaces.add(InterfaceData(reader.readU2()))
            }
            return interfaces
        }

        fun to(writer: Writer, interfaces: List<InterfaceData>) {
            for (i in interfaces) {
                writer.writeU2(i.index)
            }
        }
    }
}
