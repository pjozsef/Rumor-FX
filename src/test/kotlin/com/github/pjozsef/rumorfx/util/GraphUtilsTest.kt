package com.github.pjozsef.rumorfx.util

import com.github.pjozsef.rumorfx.datastructure.Graph
import com.github.pjozsef.rumorfx.datastructure.HashGraph
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GraphUtilsTest {
    private lateinit var graph: Graph<Int>

    @Before
    fun init() {
        graph = HashGraph<Int>().apply {
            addNode(1)
            addNode(2)
            addNode(3)
            addNode(4)
            addNode(5)
            addNode(6)
            addNode(7)
            addNode(8)
            addNode(9)
            addNode(10)

            addEdge(1, 2)
            addEdge(1, 3)
            addEdge(1, 4)
            addEdge(2, 3)
            addEdge(2, 4)
            addEdge(3, 4)
            addEdge(3, 5)
            addEdge(5, 6)
            addEdge(5, 7)
            addEdge(5, 8)
            addEdge(5, 9)
            addEdge(6, 7)
            addEdge(7, 8)
            addEdge(9, 10)
        }

    }

    @Test
    fun DELTA() {
        val DELTA = 5
        assertEquals(DELTA, graph.DELTA)
    }

    @Test
    fun delta() {
        val delta = 1
        assertEquals(delta, graph.delta)
    }

    @Test
    fun boundary() {
        val set = setOf(3, 5)
        val expected = setOf(1, 2, 4, 6, 7, 8, 9)

        assertEquals(expected, set.boundary(graph))
    }

    @Test
    fun boundarySize() {
        val set = setOf(3, 5)
        val expected = setOf(1, 2, 4, 6, 7, 8, 9)

        assertEquals(expected.size, set.boundarySize(graph))
    }

    @Test
    fun powerSet() {
        val set = setOf(1, 2, 3, 4)
        val expected = setOf(
                emptySet(),
                setOf(1),
                setOf(2),
                setOf(3),
                setOf(4),
                setOf(1, 2),
                setOf(1, 3),
                setOf(1, 4),
                setOf(2, 3),
                setOf(2, 4),
                setOf(3, 4),
                setOf(1, 2, 3),
                setOf(1, 2, 4),
                setOf(1, 3, 4),
                setOf(2, 3, 4),
                setOf(1, 2, 3, 4)
        )

        val result = set.powerSet().toSet()

        assertEquals(expected, result)
    }

    @Test
    fun vertexExpansion_set() {
        val set = setOf(3, 5)
        val expected = 7 / 2.0

        assertEquals(expected, set.vertexExpansion(graph), 0.001)
    }

    @Test
    fun volume() {
        val set = (1..10).toSet()
        val expected = 28

        assertEquals(expected, set.volume(graph))
    }

    @Test
    fun cut() {
        val set = setOf(2, 3, 7, 9)
        val expected = setOf(
                2 to 1, 1 to 2,
                2 to 4, 4 to 2,
                3 to 1, 1 to 3,
                3 to 4, 4 to 3,
                3 to 5, 5 to 3,
                7 to 5, 5 to 7,
                7 to 6, 6 to 7,
                7 to 8, 8 to 7,
                9 to 5, 5 to 9,
                9 to 10, 10 to 9)

        assertEquals(expected, graph.cut(set).toSet())
    }

    @Test
    fun cutSize() {
        val set = setOf(2, 3, 7, 9)
        val expected = setOf(
                2 to 1, 1 to 2,
                2 to 4, 4 to 2,
                3 to 1, 1 to 3,
                3 to 4, 4 to 3,
                3 to 5, 5 to 3,
                7 to 5, 5 to 7,
                7 to 6, 6 to 7,
                7 to 8, 8 to 7,
                9 to 5, 5 to 9,
                9 to 10, 10 to 9)

        assertEquals(expected.size, graph.cutSize(set))
    }
}
