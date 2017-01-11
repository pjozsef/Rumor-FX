package com.github.pjozsef.rumorfx.algorithm

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode
import com.github.pjozsef.rumorfx.datastructure.Graph
import com.github.pjozsef.rumorfx.util.findEdge
import com.github.pjozsef.rumorfx.util.isReversed
import com.github.pjozsef.rumorfx.util.random
import com.github.pjozsef.rumorfx.util.shuffle
import java.util.*

class PP0 : RumorAlgorithm() {
    override fun run(graph: Graph<GraphNode>, edges: List<GraphEdge>) {
        var rumourSpread = false
        var i = 1
        while (!rumourSpread) {
            val taboo = HashSet<GraphNode>(graph.nodes.size)
            graph.nodes.shuffle().forEach {
                if (!taboo.contains(it)) {
                    update(Action.Reset)
                    super.sleep()
                    taboo += it
                    val neighbour = graph.getNeighbours(it).toList().random()
                    val edge = edges.findEdge(it, neighbour)
                    val reversed = edge.isReversed(it, neighbour)
                    if (!taboo.contains(neighbour)) {
                        taboo += neighbour
                        if (it.selected) {
                            if (!neighbour.selected) {
                                neighbour.select()
                                update(Action.Success(edge, reversed))
                            } else {
                                update(Action.Failure(edge, reversed))
                            }
                        } else {
                            if (neighbour.selected) {
                                it.select()
                                update(Action.Success(edge, reversed))
                            } else {
                                update(Action.Failure(edge, reversed))
                            }
                        }
                    } else {
                        update(Action.Failure(edge, reversed))
                    }
                    super.sleep()
                }
            }
            rumourSpread = graph.nodes.all { it.selected }
            update(Action.NewRound(i))
            ++i
        }
        update(Action.Clear)
    }
}