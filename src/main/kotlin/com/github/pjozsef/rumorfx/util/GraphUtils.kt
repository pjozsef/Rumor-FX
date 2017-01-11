package com.github.pjozsef.rumorfx.util

import com.github.pjozsef.rumorfx.datastructure.Graph
import java.util.*

val <T> Graph<T>.DELTA: Int
    get() = this.nodeNeighbourCounts().max() ?: 0

val <T> Graph<T>.delta: Int
    get() = this.nodeNeighbourCounts().min() ?: 0

fun <T> Graph<T>.conductance(): Double {
    val nodes = this.nodes.toSet()
    val graphVolume_2 = nodes.volume(this) / 2.0

    data class SetWithVolume(val set: Set<T>, val volume: Int)

    return nodes.powerSet().map {
        SetWithVolume(it, it.volume(this))
    }.filter {
        0 < it.volume && it.volume <= graphVolume_2
    }.map {
        this.cutSize(it.set).toDouble() / it.volume.toDouble()
    }.min() ?: 0.0
}

fun <T> Graph<T>.vertexExpansion(): Double {
    val nodes = this.nodes.toSet()
    val n_2 = nodes.size.toDouble() / 2
    return nodes.powerSet().filter {
        it.isNotEmpty() && it.size <= n_2
    }.map {
        it.vertexExpansion(this)
    }.min() ?: 0.0
}

fun <T> Set<T>.boundary(graph: Graph<T>): Set<T> {
    val nodes = graph.nodes.toMutableSet()
    nodes.removeAll(this)
    return nodes.filter { graph.getNeighbours(it).intersect(this).isNotEmpty() }.toSet()
}

fun <T> Set<T>.boundarySize(graph: Graph<T>): Int = this.boundary(graph).size

fun <T> Set<T>.vertexExpansion(graph: Graph<T>): Double = this.boundarySize(graph).toDouble() / this.size

fun <T> Graph<T>.nodeNeighbourCounts(): List<Int> = this.nodes.map { getNeighbours(it).size }

fun <T> Set<T>.powerSet(): Collection<Set<T>> {
    tailrec fun <T> iter(set: List<T>, ps: Collection<List<T>>): Collection<List<T>> {
        return if (set.isEmpty()) {
            ps
        } else {
            iter(set.tail, ps + ps.map { it + set.head })
        }
    }
    val result = iter(this.toList(), mutableListOf(ArrayList<T>()))
    return result.map { it.toSet() }
}

fun <T> Set<T>.volume(graph: Graph<T>): Int {
    return this.map { graph.getNeighbours(it).size }.sum()
}

fun <T> Graph<T>.cut(fromSet: Set<T>): Collection<Pair<T, T>> {
    val toSet = this.nodes.toMutableSet()
    toSet.removeAll(fromSet)
    return this.edges.filter {
        fromSet.contains(it.first) && toSet.contains(it.second)
        || fromSet.contains(it.second) && toSet.contains(it.first)
    }
}

fun <T> Graph<T>.cutSize(set: Set<T>): Int = this.cut(set).size

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()