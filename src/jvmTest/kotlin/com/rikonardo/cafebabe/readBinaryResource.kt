package com.rikonardo.cafebabe

actual fun readBinaryResource(resourceName: String): ByteArray {
    return ::readBinaryResource::class.java.getResourceAsStream(resourceName).readBytes()
}
