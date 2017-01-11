package com.github.pjozsef.rumorfx.util

import org.junit.Assert.assertEquals
import org.junit.Test

class ListUtilsTest {

    @Test
    fun shuffle() {
        val input = listOf(1, 2, 3, 4, 5)
        val expected = setOf(2, 1, 3, 5, 4)

        assertEquals(expected, input.shuffle().toSet())
    }
}
