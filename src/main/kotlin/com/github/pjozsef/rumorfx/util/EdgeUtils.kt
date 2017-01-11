package com.github.pjozsef.rumorfx.util

import com.github.pjozsef.rumorfx.component.graph.GraphEdge
import com.github.pjozsef.rumorfx.component.graph.GraphNode

fun List<GraphEdge>.findEdge(from: GraphNode, to: GraphNode): GraphEdge {
    val result = this.firstOrNull { (it.from == from && it.to == to) || (it.from == to && it.to == from) }
    return result ?: error("Could not find edge, from: $from, to: $to")
}

fun GraphEdge.isReversed(from: GraphNode, to: GraphNode): Boolean{
    if(this.from == from && this.to == to){
        return false
    }else if(this.from == to && this.to == from){
        return true
    }else{
        error("$from and $to are not endpoints of $this")
    }
}