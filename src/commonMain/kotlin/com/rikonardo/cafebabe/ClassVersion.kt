package com.rikonardo.cafebabe

class ClassVersion(private val classFile: ClassFile) {
    var minor
        get() = classFile.data.minorVersion
        set(value) {
            classFile.data.minorVersion = value
        }
    var major
        get() = classFile.data.majorVersion
        set(value) {
            classFile.data.majorVersion = value
        }

    override fun toString(): String {
        return "$major.$minor"
    }
}
