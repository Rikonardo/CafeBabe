<div align="center"><h1>CafeBabe - Java .class files parser for Kotlin</h1></div>

<div align="center"><img alt="Logo" src="logo.svg" width="512"/></div>

<div align="center">
    <img alt="Open issues" src="https://img.shields.io/github/issues-raw/Rikonardo/CafeBabe"/>
    <img alt="Open issues" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.rikonardo.com%2Freleases%2Fcom%2Frikonardo%2Fcafebabe%2FCafeBabe%2Fmaven-metadata.xml"/>
    <img alt="Open issues" src="https://img.shields.io/github/languages/code-size/Rikonardo/CafeBabe"/>
</div>

<br>

<hr>

**CafeBabe** is a Java .class file parser/serializer written in pure Kotlin multiplatform. It doesn't provide any tools for working with Java bytecode, but it does allow you to manipulate the class structure and metadata.

💼 **This readme contains full library documentation/tutorial!**

## Install

Gradle Kotlin:
```kotlin
repositories {
    maven {
        url = uri("https://maven.rikonardo.com/releases")
    }
}

dependencies {
    implementation("com.rikonardo.cafebabe:CafeBabe:1.0.1")
}
```

## Documentation

| Content                                    |
|--------------------------------------------|
| **1. [Parsing classes](#parsing-classes)** |
| **2. [Constant pool](#constant-pool)**     |
| **3. [Editing classes](#editing-classes)** |
| **4. [Interfaces](#interfaces)**           |
| **5. [Fields](#fields)**                   |
| **6. [Methods](#methods)**                 |
| **7. [Attributes](#attributes)**           |

## Parsing classes
To parse a class, call the `ClassFile` constructor with a ByteArray parameter:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    println(classFile.name) // Prints class name (e.g. "com/example/MyClass")
    println(classFile.parent) // Prints parent class name (e.g. "java/lang/Object")
    println(classFile.version.toString()) // Prints class version (e.g. "52.0")
    println(classFile.access.toString()) // Prints class access flags (e.g. "PUBLIC, SYNCHRONIZED")
    println(classFile) // Prints all class metadata
}
```

You can also retrieve raw class or class members' metadata by accessing the `data` property:
    
```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    println(classFile.data) // Prints all class raw metadata
    println(classFile.data.fields) // Prints all class fields raw metadata
    println(classFile.data.methods) // Prints all class methods raw metadata
}
```

## Constant pool
The constant pool is a list of all constants used in the class. Constants are all values or value literals that are used in the class. Their peculiarity is that they are numerated starting from 1, not 0. CafeBabe provides wrapper for constant list, so you can access them directly by index:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val constant = classFile.constantPool[1]
    println(constant) // Prints constant at index 1, which is the first constant in constant pool
}
```

This wrapper also has `add` method, which allows you to add new constant to the constant pool. This method returns index of the new constant:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val constant = classFile.constantPool.add(ConstantUtf8("new_constant"))
    println(constant) // Prints index of the new constant
}
```

## Editing classes
CafeBabe allows you not only read classes, but also edit them. For example, you can rename class members, or class itself; copy methods from other classes; change class members' visibility; etc.

Here is an example of renaming class and copying method from another class to it:

```kotlin
fun main(args: Array<String>) {
    val classFile = ClassFile(File("./path/to/class.class").readBytes()) // Read original class
    
    classFile.name = "com/example/MyClass" // Rename class
    
    val donorClass = ClassFile(File("./path/to/donor.class").readBytes()) // Read donor class
    val sourceMethodName = "donorMethod" // Name of method we are copying
    val targetMethodName = "targetMethod" // New name of copied method inside our class

    val nameIndex = classInfo.constantPool.add(ConstantUtf8(targetMethodName)) // Add new name to constant pool
    val method = classInfo.methods.find { it.name == sourceMethodName }
        ?: throw IllegalStateException("No method found") // Find method in donor class
    
    val newMethodData = MethodData( // Create new method data
        accessFlags = method.data.accessFlags,
        nameIndex = nameIndex, // Copy everything except name, which we changed
        descriptorIndex = method.data.descriptorIndex,
        attributes = method.data.attributes
    )
    
    val newMethod = Method(classInfo, newMethodData) // Create new method
    classInfo.methods.add(newMethod) // Add new method to class
    
    File("./path/to/class.class").writeBytes(classFile.compile()) // Write class back to file
}
```

❗ Note that copied method won't work as expected because it's bytecode references to the constant pool entries in source class.

Changing class or class member name will automatically change value in the constant pool on related index. This can lead to unexpected behavior when single Utf8 constant used in multiple places, so it's recommended to create separate constant pool entry when renaming:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    
    val constantClass = classFile.constantPool[classFile.data.thisClass] as ConstantClass // Get class constant
    constantClass.nameIndex = classFile.constantPool.add(ConstantUtf8("")) // Replace linked name constant
    classFile.name = "com/example/MyClass" // Rename class
    
    File("./path/to/class.class").writeBytes(classFile.compile())
}
```

## Interfaces
You can get list of interfaces implemented by a class by calling `classFile.interfaces` property:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val i = classFile.interfaces[0]
    println(i.name) // Prints interface name (e.g. "java/lang/Runnable")
}
```

## Fields
You can get list of fields by calling `classFile.fields` property:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val f = classFile.fields[0]
    println(f.name) // Prints field name (e.g. "fieldName")
    println(f.access) // Prints field access flags (e.g. "PUBLIC")
    println(f.descriptor) // Prints field descriptor (e.g. "Ljava/lang/String;")
    println(f.attributes) // Print attributes (more on them later)
}
```

## Methods
You can get list of methods by calling `classFile.methods` property:

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val m = classFile.methods[0]
    println(m.name) // Prints method name (e.g. "methodName")
    println(m.access) // Prints method access flags (e.g. "PUBLIC")
    println(m.descriptor) // Prints method descriptor (e.g. "()V")
    println(m.attributes) // Print attributes (more on them later)
}
```

## Attributes
Attributes contain data, that is related to JVM runtime. For example, they can contain method code, annotations data, etc. CafeBabe doesn't parse attributes, but provides their names and ByteArray body, so you can manually parse and modify them if you need.

```kotlin
fun main(args: Array<String>) {
    val classBytes = File("./path/to/class.class").readBytes()
    val classFile = ClassFile(classBytes)
    val m = classFile.methods[0]
    val a = m.attributes[0]
    println(a.name) // Prints attribute name (e.g. "Code")
    println(a.info.joinToString("") { "%02x".format(it) }) // Prints attribute body as hex string
}
```