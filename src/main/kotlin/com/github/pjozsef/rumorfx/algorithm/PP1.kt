package com.github.pjozsef.rumorfx.algorithm

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode
import com.github.pjozsef.rumorfx.datastructure.Graph
import com.github.pjozsef.rumorfx.util.*
import java.util.*

class PP1 : RumorAlgorithm() {
    override fun run(graph: Graph<GraphNode>, edges: List<GraphEdge>) {
        var rumourSpread = false
        var i = 1
        while (!rumourSpread) {
            val taboo = HashSet<GraphNode>(graph.nodes.size)
            graph.nodes.shuffle().forEach {
                if (!taboo.contains(it)) {
                    update(Action.Reset)
                    taboo += it
                    val (selectedNeighbours, unselectedNeighbours) = graph.getNeighbours(it).partition { it.selected }
                    val neighbour: GraphNode?
                    if(it.selected){
                        neighbour = unselectedNeighbours.safeRandom()
                    }else{
                        neighbour = selectedNeighbours.safeRandom()
                    }
                    if (neighbour != null) {
                        super.sleep()
                        val edge = edges.findEdge(it, neighbour)
                        val reversed = edge.isReversed(it, neighbour)
                        if (!taboo.contains(neighbour)) {
                            taboo += neighbour
                            if (it.selected) {
                                neighbour.select()
                                update(Action.Success(edge, reversed))
                            } else {
                                it.select()
                                update(Action.Success(edge, reversed))
                            }
                        } else {
                            update(Action.Failure(edge, reversed))
                        }
                        super.sleep()
                    }
                }
            }
            rumourSpread = graph.nodes.all { it.selected }
            update(Action.NewRound(i))
            ++i
        }
        update(Action.Clear)
    }
}