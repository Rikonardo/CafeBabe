package com.rikonardo.cafebabe

import com.rikonardo.cafebabe.data.MethodData
import com.rikonardo.cafebabe.data.constantpool.ConstantUtf8
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun simpleRecompileTest() {
        val binary = readBinaryResource("/classes/HelloWorld.class")
        val classInfo = ClassFile(binary)
        val newBinary = classInfo.compile()
        assertTrue(binary.contentEquals(newBinary))
    }

    @Test
    fun renameClassTest() {
        val binary = readBinaryResource("/classes/HelloWorld.class")
        val classInfo = ClassFile(binary)

        val newName = classInfo.name.let {
            val parts = classInfo.name.split("/").toMutableList()
            parts[parts.size - 1] = "NewName"
            parts.joinToString("/")
        }
        classInfo.name = newName

        val newBinary = classInfo.compile()
        val newClassInfo = ClassFile(newBinary)
        assertEquals(newClassInfo.name, newName)
    }

    @Test
    fun copyMethodTest() {
        val binary = readBinaryResource("/classes/HelloWorld.class")
        val classInfo = ClassFile(binary)

        val nameIndex = classInfo.constantPool.add(ConstantUtf8("myBrandNewMethod"))
        val method = classInfo.methods.find { it.name == "sayHello" } ?: throw IllegalStateException("No method found")
        val newMethodData = MethodData(
            method.data.accessFlags,
            nameIndex = nameIndex,
            method.data.descriptorIndex,
            method.data.attributes
        )
        val newMethod = Method(classInfo, newMethodData)
        classInfo.methods.add(newMethod)

        val newBinary = classInfo.compile()
        val newClassInfo = ClassFile(newBinary)

        assertTrue(newClassInfo.methods.any { it.name == "myBrandNewMethod" })
    }
}
