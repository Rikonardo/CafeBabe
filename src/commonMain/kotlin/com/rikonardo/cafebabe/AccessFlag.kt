package com.rikonardo.cafebabe

enum class AccessFlag(val value: Int) {
    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SYNCHRONIZED(0x0020),
    VOLATILE(0x0040),
    TRANSIENT(0x0080),
    NATIVE(0x0100),
    INTERFACE(0x0200),
    ABSTRACT(0x0400),
    STRICT(0x0800),
    SYNTHETIC(0x1000),
    ANNOTATION(0x2000),
    ENUM(0x4000);

    companion object {
        fun from(value: Int): List<AccessFlag> {
            return values().filter { it.value and value == it.value }
        }

        fun to(value: List<AccessFlag>): Int {
            return value.fold(0) { acc, flag -> acc or flag.value }
        }
    }
}
