package com.rikonardo.cafebabe.utils

class ObservableList<T>(list: List<T>) : MutableList<T> {
    private val list = mutableListOf<T>()
    private val listeners = mutableListOf<(List<T>) -> Unit>()
    private fun notifyListeners() {
        listeners.forEach { it(this) }
    }

    fun addListener(listener: (List<T>) -> Unit) {
        listeners.add(listener)
    }

    init {
        this.list.addAll(list)
    }

    override val size: Int
        get() = list.size

    override fun add(index: Int, element: T) {
        list.add(index, element)
        notifyListeners()
    }

    override fun add(element: T): Boolean {
        val result = list.add(element)
        notifyListeners()
        return result
    }

    override fun clear() {
        list.clear()
        notifyListeners()
    }

    override fun contains(element: T): Boolean {
        return list.contains(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = list.addAll(elements)
        notifyListeners()
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = list.addAll(index, elements)
        notifyListeners()
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val result = list.removeAll(elements)
        notifyListeners()
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val result = list.retainAll(elements)
        notifyListeners()
        return result
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return list.containsAll(elements)
    }

    override fun get(index: Int): T {
        return list[index]
    }

    override fun indexOf(element: T): Int {
        return list.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun iterator(): MutableIterator<T> {
        return list.iterator()
    }

    override fun lastIndexOf(element: T): Int {
        return list.lastIndexOf(element)
    }

    override fun listIterator(): MutableListIterator<T> {
        return list.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return list.listIterator(index)
    }

    override fun remove(element: T): Boolean {
        val result = list.remove(element)
        notifyListeners()
        return result
    }

    override fun removeAt(index: Int): T {
        val result = list.removeAt(index)
        notifyListeners()
        return result
    }

    override fun set(index: Int, element: T): T {
        val result = list.set(index, element)
        notifyListeners()
        return result
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return list.subList(fromIndex, toIndex)
    }

    override fun equals(other: Any?): Boolean {
        return list == other
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }

    override fun toString(): String {
        return list.toString()
    }
}
