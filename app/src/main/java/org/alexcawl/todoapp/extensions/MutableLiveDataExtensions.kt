package org.alexcawl.todoapp.extensions

import androidx.lifecycle.MutableLiveData

@Throws(IndexOutOfBoundsException::class)
operator fun <T> MutableLiveData<MutableList<T>>.get(index: Int): T {
    return this.value?.get(index) ?: throw IndexOutOfBoundsException()
}

@Throws(IndexOutOfBoundsException::class)
operator fun <T> MutableLiveData<MutableList<T>>.set(index: Int, value: T) {
    this.value?.set(index, value) ?: throw IndexOutOfBoundsException()
}

fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.add(index: Int, item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(index, item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.removeAt(index: Int) {
    if (!this.value.isNullOrEmpty()) {
        val oldValue = this.value
        oldValue?.removeAt(index)
        this.value = oldValue
    } else {
        this.value = mutableListOf()
    }
}

fun <T> MutableLiveData<MutableList<T>>.getSize(): Int {
    return this.value?.size ?: 0
}