package com.github.pjozsef.rumorfx.component.load

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode
import javafx.geometry.Point2D
import java.util.*


enum class LoadPreset {
    BASE {
        override val nodes: List<GraphNode>
        override val edges: List <GraphEdge>

        private val MARGIN = 130.0
        private val OFFSET = 65.0

        init {
            nodes = generateNodes()
            edges = generateEdges()
        }

        private fun generateNodes(): List<GraphNode> {
            val result = ArrayList<GraphNode>()
            for (y in 0..5) {
                for (x in 0..10) {
                    result += GraphNode(MARGIN + x * OFFSET, MARGIN + y * OFFSET)
                }
            }
            return result
        }

        private fun generateEdges(): List<GraphEdge> {
            val result = ArrayList<GraphEdge>()
            for (node1 in nodes) {
                for (node2 in nodes) {
                    if (node1 != node2 && areDirectNeighbours(node1, node2)) {
                        result += GraphEdge(node1, node2)
                    }
                }
            }
            return result
        }

        private fun areDirectNeighbours(node1: GraphNode, node2: GraphNode): Boolean {
            val point1 = Point2D(node1.x, node1.y)
            val point2 = Point2D(node2.x, node2.y)
            return point1.distance(point2) == OFFSET
        }
    };

    protected fun n(x: Int, y: Int) = GraphNode(x.toDouble(), y.toDouble())
    protected fun e(i: Int, j: Int) = GraphEdge(nodes[i], nodes[j])

    abstract val nodes: List<GraphNode>
    abstract val edges: List<GraphEdge>
}