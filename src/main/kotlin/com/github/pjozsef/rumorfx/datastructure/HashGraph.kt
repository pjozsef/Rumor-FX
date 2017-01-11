package com.github.pjozsef.rumorfx.datastructure

import java.util.*

class HashGraph<N>(val direction: Direction = Direction.UNDIRECTED) : Graph<N> {
    companion object {
        enum class Direction {DIRECTED, UNDIRECTED }

        private const val DEFAULT_SIZE = 30
    }

    private val map = HashMap<N, MutableSet<N>>(DEFAULT_SIZE)

    override val size: Int
        get() = map.size

    override val nodes: Collection<N>
        get() = HashSet(map.keys)

    override val edges: Collection<Pair<N, N>>
        get(){
            return map.keys.flatMap { key ->
                map[key]!!.map { value ->
                    key to value
                }
            }
        }

    override fun addNode(node: N) {
        map.put(node, mutableSetOf())
    }

    override fun deleteNode(node: N) {
        map.values.forEach { it.remove(node) }
        map.keys.remove(node)
    }

    override fun addEdge(from: N, to: N) {
        map[from]?.add(to) ?: throw noSuchElement(from)
        if (direction == Direction.UNDIRECTED) {
            map[to]?.add(from) ?: throw noSuchElement(to)
        }
    }

    override fun deleteEdge(from: N, to: N) {
        map[from]?.remove(to) ?: throw noSuchElement(from)
        if (direction == Direction.UNDIRECTED) {
            map[to]?.remove(from) ?: throw noSuchElement(to)
        }
    }

    override fun getNeighbours(node: N) = map[node] ?: throw noSuchElement(node)

    override fun clear(){
        map.clear()
    }

    fun copy(): HashGraph<N>{
        val copy = HashGraph<N>(direction)
        copy.map.putAll(map)
        return copy
    }

    private fun noSuchElement(node: N) = NoSuchElementException("No such element: $node")

    override fun toString(): String {
        return map.entries
                .mapIndexed { i, entry ->
                    "[${entry.key}] -> ${entry.value.joinToString()}"
                }.joinToString("\n")
    }
}