package com.github.pjozsef.rumorfx.util

import javafx.geometry.Point2D
import javafx.scene.shape.Line

fun Line.intersectionPoint(other: Line): Point2D {
    val x1 = this.startX
    val y1 = this.startY
    val x2 = this.endX
    val y2 = this.endY
    val x3 = other.startX
    val y3 = other.startY
    val x4 = other.endX
    val y4 = other.endY

    val x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4))
    val y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4))
    return Point2D(x, y)
}

fun Line.containsPoint(point: Point2D): Boolean{
    val start = Point2D(startX, startY)
    val end = Point2D(endX, endY)
    val firstHalf = start.distance(point)
    val secondHalf = end.distance(point)
    val whole = start.distance(end)
    return Math.abs(firstHalf + secondHalf - whole) < 1e-2
}

fun Line.intersects(other: Line): Boolean {
    val point = this.intersectionPoint(other)
    return this.containsPoint(point) && other.containsPoint(point)
}