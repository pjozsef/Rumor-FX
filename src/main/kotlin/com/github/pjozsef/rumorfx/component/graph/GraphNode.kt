package com.github.pjozsef.rumorfx.component.graph

import com.github.pjozsef.rumorfx.Styles
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.DoubleProperty
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import tornadofx.plusAssign

class GraphNode(x: Double, y: Double) : Group() {
    companion object {
        private const val DIAMETER = 15.0
        private const val RADIUS = DIAMETER / 2.0
    }

    private val selection = Circle(x, y, 0.0, Styles.Companion.green)
    private val base = Circle(x, y, 0.0, Styles.Companion.green)
    val xProperty: DoubleProperty = base.centerXProperty()
    val yProperty: DoubleProperty = base.centerYProperty()
    val x: Double
        get() = xProperty.value
    val y: Double
        get() = yProperty.value

    var selected = false
        private set

    init {
        children.addAll(selection, base)
        setOnMouseDragged {
            if (!it.isShiftDown) {
                selection.centerX = it.x
                selection.centerY = it.y
                base.centerX = it.x
                base.centerY = it.y
            }
        }
    }

    fun show() {
        val timeline = Timeline()
        val kv = KeyValue(base.radiusProperty(), DIAMETER, Interpolator.EASE_BOTH)
        val kf = KeyFrame(Duration.millis(50.0), kv)
        timeline.keyFrames += kf
        timeline.setOnFinished {
            selection.radius = base.radius
        }
        timeline.play()
    }

    fun select() {
        val timeline = Timeline()
        timeline.keyFrames += KeyFrame(
                Duration.millis(100.0),
                KeyValue(selection.radiusProperty(), base.radius * 1.3, Interpolator.EASE_BOTH),
                KeyValue(base.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH))
        timeline.play()
        selected = true
    }

    fun deselect() {
        val timeline = Timeline()
        timeline.keyFrames += KeyFrame(
                Duration.millis(100.0),
                KeyValue(selection.radiusProperty(), base.radius, Interpolator.EASE_BOTH),
                KeyValue(base.fillProperty(), Styles.Companion.green, Interpolator.EASE_BOTH))
        timeline.play()
        selected = false
    }

    fun activate(){
        base.fill = Styles.active
    }

    fun deactivate(){
        if(selected) base.fill = Styles.white else base.fill = Styles.green
    }

    fun removeAnimation(onFinish: () -> Unit){
        val timeline = Timeline()
        timeline += KeyFrame(Duration.millis(100.0),
                KeyValue(base.radiusProperty(), 0.0, Interpolator.EASE_BOTH),
                KeyValue(selection.radiusProperty(), 0.0, Interpolator.EASE_BOTH))
        timeline.setOnFinished { onFinish() }
        timeline.play()
    }

    override fun toString() = "(${base.centerX},${base.centerY})"
}