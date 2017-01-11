package com.github.pjozsef.rumorfx.datastructure

interface Graph<N> {
    val size: Int
    val nodes: Collection<N>
    val edges: Collection<Pair<N,N>>
    fun addNode(node: N)
    fun deleteNode(node: N)
    fun addEdge(from: N, to: N)
    fun deleteEdge(from: N, to: N)
    fun getNeighbours(node: N): Collection<N>
    fun clear()

    //Operators
    operator fun plus(node: N): Graph<N> {
        addNode(node)
        return this
    }

    operator fun plusAssign(node: N) {
        addNode(node)
    }

    operator fun minus(node: N): Graph<N> {
        deleteNode(node)
        return this
    }

    operator fun minusAssign(node: N) {
        deleteNode(node)
    }
}