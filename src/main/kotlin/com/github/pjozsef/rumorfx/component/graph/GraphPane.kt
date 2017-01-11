package com.github.pjozsef.rumorfx.component.graph

import com.github.pjozsef.rumorfx.Styles
import com.github.pjozsef.rumorfx.datastructure.HashGraph
import com.github.pjozsef.rumorfx.util.*
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import tornadofx.success
import tornadofx.task
import java.util.*

class GraphPane : AnchorPane() {


    val circles = ArrayList<GraphNode>()
    val edges = ArrayList<GraphEdge>()
    private var baseX = 0.0
    private var baseY = 0.0
    private val dragLine = Line(0.0, 0.0, 0.0, 0.0)

    val graph = HashGraph<GraphNode>()

    val expansion: DoubleProperty = SimpleDoubleProperty(0.0)
    val conductance: DoubleProperty = SimpleDoubleProperty(0.0)
    val delta: IntegerProperty = SimpleIntegerProperty(0)
    val DELTA: IntegerProperty = SimpleIntegerProperty(0)

    init {
        children += dragLine

        setOnMousePressed {
            val circle = boundingCircle(it)
            if (circle != null) {
                baseX = circle.x
                baseY = circle.y
            } else {
                baseX = it.x
                baseY = it.y
            }
        }

        setOnMouseClicked {
            if (it.isStillSincePress) {
                val circle = boundingCircle(it)
                if (circle != null) {
                    if (it.isControlDown) {
                        circle.removeAnimation {
                            removeNode(circle)
                        }
                    } else if (!circle.selected) {
                        circles.forEach { it.deselect() }
                        circle.select()
                    }
                } else {
                    addNode(it)
                }
            }
        }

        setOnMouseDragged {
            if (it.isShiftDown) {
                val circle = boundingCircle(it)
                if (circle != null) {
                    dragLine.set(baseX, baseY, circle.x, circle.y)
                } else {
                    dragLine.set(baseX, baseY, it.x, it.y)
                }
            }
        }

        setOnMouseReleased {
            val startcircle = boundingCircle(baseX, baseY)
            val endcircle = boundingCircle(it)
            if (it.isShiftDown) {
                if (startcircle != null && endcircle != null && startcircle != endcircle) {
                    if (isNewEdge(startcircle, endcircle)) {
                        addEdge(startcircle, endcircle)
                    }
                } else if (startcircle == null && endcircle == null) {
                    val line = Line(baseX, baseY, it.x, it.y)
                    edges.filter { line.intersects(it) }
                            .forEach {
                                it.removeAnimation {
                                    removeEdge(it)
                                }
                            }
                }
            }
            dragLine.reset()
        }

    }

    fun clear(onFinish: ()->Unit = {}) {
        val overlay = Rectangle(width, height, Styles.Companion.transparent)
        val ripple = Circle(width / 2, height / 2, 0.0, Styles.Companion.backgroundDark)
        children += ripple
        children += overlay
        val maxRadius = width / 2 - 10
        val timeline = Timeline(
                KeyFrame(
                        Duration.millis(500.0),
                        KeyValue(ripple.radiusProperty(), maxRadius, Interpolator.EASE_BOTH),
                        KeyValue(overlay.fillProperty(), Styles.Companion.background, Interpolator.SPLINE(0.0, 0.0, 0.7, 0.8))
                ))
        timeline.setOnFinished {
            graph.clear()
            circles.clear()
            edges.clear()
            children.clear()
            children += dragLine
            onFinish()
        }
        timeline.play()
    }

    fun copyGraph() = graph.copy()

    fun copyEdges(): List<GraphEdge> = ArrayList(edges)

    private fun addNode(mouse: MouseEvent){
        addNode(GraphNode(mouse.x, mouse.y))
    }

    fun addNode(node: GraphNode) {
        children += node
        circles += node
        graph += node
        node.show()
    }

    private fun removeNode(node: GraphNode) {
        children -= node
        circles -= node
        val edgesToRemove = edges.filter { it.hasNode(node) }
        if (edgesToRemove.size > 0) {
            var count = 1
            edgesToRemove.forEach {
                it.removeAnimation(node) {
                    removeEdge(it)
                    if (count == edgesToRemove.size) {
                        graph -= node
                    }
                    count++
                }
            }
        } else {
            graph -= node
        }

    }

    fun addEdge(graphEdge: GraphEdge){
        addEdge(graphEdge.from, graphEdge.to)
    }

    private fun addEdge(startNode: GraphNode, endNode: GraphNode) {
        val edge = GraphEdge(startNode, endNode)
        edges += edge
        children += edge
        graph.addEdge(startNode, endNode)
        edge.toBack()
        edge.show()
    }

    private fun removeEdge(it: GraphEdge) {
        edges -= it
        children -= it
        graph.deleteEdge(it.from, it.to)
    }

    private fun alreadyContainsEdge(start: GraphNode, end: GraphNode): Boolean {
        return edges.any {
            it.from == start && it.to == end
                    || it.from == end && it.to == start
        }
    }

    private fun isNewEdge(start: GraphNode, end: GraphNode) = !alreadyContainsEdge(start, end)

    private fun boundingCircle(mouse: MouseEvent) = boundingCircle(mouse.x, mouse.y)

    private fun boundingCircle(x: Double, y: Double) = boundingCircle(Point2D(x, y))

    private fun boundingCircle(point: Point2D): GraphNode? {
        return circles.find { it.contains(point) }
    }

    private fun Line.set(startX: Double, startY: Double, endX: Double, endY: Double) {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY
    }

    private fun Line.reset() {
        this.set(0.0, 0.0, 0.0, 0.0)
    }
}