package com.github.pjozsef.rumorfx.component.load

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode
import com.github.pjozsef.rumorfx.component.graph.GraphPane
import javafx.application.Platform
import tornadofx.task


class GraphLoader(val graphPane: GraphPane) {
    private val LONG_SLEEP = 15L
    private val SLEEP = 5L

    fun load(preset: LoadPreset){
        task {
            preset.nodes.forEach { add(it) }
            preset.edges.forEach { add(it) }
        }
    }

    private fun add(node: GraphNode){
        Platform.runLater {
            graphPane.addNode(node)
        }
        Thread.sleep(LONG_SLEEP)
    }

    private fun add(edge: GraphEdge){
        Platform.runLater {
            graphPane.addEdge(edge)
        }
        Thread.sleep(SLEEP)
    }
}