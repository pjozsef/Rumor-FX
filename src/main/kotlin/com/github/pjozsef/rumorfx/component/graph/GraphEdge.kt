package com.github.pjozsef.rumorfx.component.graph

import com.github.pjozsef.rumorfx.Styles
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.util.Duration
import tornadofx.plusAssign

class GraphEdge(val from: GraphNode, val to: GraphNode) : Line() {
    companion object {

    }

    init {
        strokeWidth = 1.0
        stroke = Styles.darkgreen
        startXProperty().bind(from.xProperty)
        startYProperty().bind(from.yProperty)
        endXProperty().bind(to.xProperty)
        endYProperty().bind(to.yProperty)
    }

    fun hasNode(node: GraphNode) = from == node || to == node

    fun show() {
        val timeline = Timeline()
        timeline += KeyFrame(Duration.millis(100.0), KeyValue(strokeWidthProperty(), 3.0, Interpolator.EASE_BOTH))
        timeline.play()
    }

    fun removeAnimation(onFinish: () -> Unit) {
        removeAnimation(from) { onFinish() }
    }

    fun removeAnimation(startNode: GraphNode, onFinish: () -> Unit) {
        var propertyX = startXProperty()
        var propertyY = startYProperty()
        var endValueX = endX
        var endValueY = endY

        if(startNode==to){
            propertyX = endXProperty()
            propertyY = endYProperty()
            endValueX = startX
            endValueY = startY
        }

        startXProperty().unbind()
        startYProperty().unbind()
        endXProperty().unbind()
        endYProperty().unbind()
        val timeline = Timeline()
        timeline += KeyFrame(Duration.millis(200.0),
                KeyValue(propertyX, endValueX, Interpolator.EASE_BOTH),
                KeyValue(propertyY, endValueY, Interpolator.EASE_BOTH),
                KeyValue(strokeWidthProperty(), 0.0, Interpolator.EASE_BOTH))
        timeline.setOnFinished { onFinish() }
        timeline.play()
    }

    fun activateOk(){
        activate()
    }

    fun activateError(){
        activate(Styles.red)
    }

    fun activate(color: Color = Styles.green){
        val timeline = Timeline()
        timeline += KeyFrame(Duration.millis(200.0),
                KeyValue(strokeWidthProperty(), 6.0, Interpolator.EASE_BOTH),
                KeyValue(strokeProperty(), color, Interpolator.EASE_BOTH))
        timeline.play()
    }

    fun deactivate(){
        val timeline = Timeline()
        timeline += KeyFrame(Duration.millis(200.0),
                KeyValue(strokeWidthProperty(), 3.0, Interpolator.EASE_BOTH),
                KeyValue(strokeProperty(), Styles.darkgreen, Interpolator.EASE_BOTH))
        timeline.play()
    }
}