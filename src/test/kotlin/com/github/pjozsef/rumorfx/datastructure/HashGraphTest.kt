package com.github.pjozsef.rumorfx.datastructure

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HashGraphTest {
    lateinit var graph: HashGraph<Int>

    @Before
    fun init() {
        graph = HashGraph()
    }

    @Test
    fun size_empty() {
        assertEquals(0, graph.size)
    }

    @Test
    fun size_notEmpty() {
        addElements()
        assertEquals(3, graph.size)
    }

    @Test
    fun size_notChangedWithEdgeAddition() {
        addElements()
        addEdges()
        assertEquals(3, graph.size)
    }

    @Test
    fun addNode_duplicates() {
        graph.addNode(3)
        graph.addNode(3)
        graph.addNode(3)
        assertEquals(1, graph.size)
    }

    @Test
    fun nodes_emptyGraph() {
        assertEquals(setOf<Int>(), graph.nodes)
    }

    @Test
    fun nodes() {
        addElements()
        assertEquals(setOf(1, 2, 3), graph.nodes)
    }

    @Test
    fun edges_emptyGraph() {
        assertEquals(listOf<Int>(), graph.edges)
    }

    @Test
    fun edges() {
        addElements()
        addEdges()

        val expected = setOf(
                1 to 2,
                2 to 1,
                1 to 3,
                3 to 1,
                2 to 3,
                3 to 2
        )
        val result = graph.edges
        assertEquals(expected.size, result.size)
        assertEquals(expected, result.toSet())
    }

    @Test
    fun deleteNode() {
        addElements()
        graph.deleteNode(2)

        val expectedSet = setOf(1, 3)

        assertEquals(2, graph.size)
        assertEquals(expectedSet, graph.nodes)
    }

    @Test
    fun deleteNode_edgeUpdate() {
        addElements()
        addEdges()
        graph.deleteNode(2)

        val expectedSet = setOf(1 to 3, 3 to 1)

        assertEquals(2, graph.size)
        assertEquals(expectedSet, graph.edges.toSet())
    }

    @Test
    fun deleteEdge() {
        addElements()
        addEdges()
        graph.deleteEdge(1, 3)

        val expected = setOf(
                1 to 2,
                2 to 1,
                2 to 3,
                3 to 2
        )
        val result = graph.edges
        assertEquals(expected.size, result.size)
        assertEquals(expected, result.toSet())
    }

    @Test
    fun neighbours() {
        addElements()
        addEdges()

        val neighbours_1 = setOf(2, 3)
        val neighbours_2 = setOf(1, 3)

        val expected_1 = graph.getNeighbours(1)
        val expected_2 = graph.getNeighbours(2)

        assertEquals(expected_1, neighbours_1)
        assertEquals(expected_2, neighbours_2)
    }

    @Test
    fun clear(){
        addElements()
        addEdges()
        graph.clear()

        assertEquals(0, graph.size)
    }

    private fun addElements() {
        graph.addNode(3)
        graph.addNode(2)
        graph.addNode(1)
    }

    private fun addEdges() {
        graph.addEdge(1, 2)
        graph.addEdge(1, 3)
        graph.addEdge(2, 3)
    }
}
