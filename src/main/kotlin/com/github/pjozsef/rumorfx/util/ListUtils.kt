package com.github.pjozsef.rumorfx.util

import java.util.*

fun <T> Collection<T>.shuffle(): List<T> {
    val result = ArrayList<T>(this.size)
    val internal = this.toMutableList()

    while (internal.size > 0) {
        val elem = internal.random()
        internal.remove(elem)
        result += elem
    }

    return result
}
