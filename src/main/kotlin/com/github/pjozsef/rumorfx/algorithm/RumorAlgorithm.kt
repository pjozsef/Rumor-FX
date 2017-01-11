package com.github.pjozsef.rumorfx.algorithm

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode
import com.github.pjozsef.rumorfx.datastructure.Graph
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.LongBinding

abstract class RumorAlgorithm {
    sealed class Action {
        object Reset : Action()
        object Clear : Action()
        class NewRound(val round: Int) : Action()
        class Failure(val edge: GraphEdge, val reversed: Boolean) : Action()
        class Success(val edge: GraphEdge, val reversed: Boolean) : Action()
    }

    lateinit var sleep: LongBinding
    lateinit var animate: BooleanBinding
    lateinit var updateTask: (Action) -> Unit

    abstract fun run(graph: Graph<GraphNode>, edges: List<GraphEdge>)

    protected fun update(action: Action) {
        when (action) {
            is Action.Clear, is Action.NewRound -> updateTask(action)
            else -> if (animate.value) {
                updateTask(action)
            }
        }
    }

    protected fun sleep() {
        if (animate.value) {
            Thread.sleep(sleep.value)
        }
    }
}