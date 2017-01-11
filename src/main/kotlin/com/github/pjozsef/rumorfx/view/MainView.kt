package com.github.pjozsef.rumorfx.view

import com.github.pjozsef.rumorfx.Styles
import com.github.pjozsef.rumorfx.algorithm.*
import com.github.pjozsef.rumorfx.algorithm.RumorAlgorithm.Action.*
import com.github.pjozsef.rumorfx.component.graph.GraphPane
import com.github.pjozsef.rumorfx.component.load.GraphLoader
import com.github.pjozsef.rumorfx.component.load.LoadPreset
import com.github.pjozsef.rumorfx.util.findEdge
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXSlider
import javafx.application.Platform
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.LongBinding
import javafx.event.ActionEvent
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.CornerRadii
import tornadofx.View
import tornadofx.booleanBinding
import tornadofx.longBinding
import tornadofx.task

class MainView : View("Rumor spreading") {
    override val root: BorderPane by fxml("MainView.fxml")

    val graphPane: GraphPane by fxid()
    val clear: JFXButton by fxid()
    val push: JFXButton by fxid()
    val pull: JFXButton by fxid()
    val pp0: JFXButton by fxid()
    val pp1: JFXButton by fxid()
    val animSlider: JFXSlider by fxid()
    val rounds: Label by fxid()
    val load: JFXButton by fxid()

    val sleepBinding: LongBinding
    val showAnimationBinding: BooleanBinding

    init {
        initButtons()

        sleepBinding = animSlider.valueProperty().longBinding { it?.toLong() ?: 0 }
        showAnimationBinding = animSlider.valueProperty().booleanBinding {
            if (it == null) false
            else it.toLong() > 0
        }
    }

    private fun initButtons() {
        clear.setOnAction {
            graphPane.clear()
            rounds.text = ""
        }

        load.setOnAction {
            val loader = GraphLoader(graphPane)
            if(graphPane.graph.size>0){
                rounds.text = ""
                graphPane.clear{
                    loader.load(LoadPreset.BASE)
                }
            }else{
                loader.load(LoadPreset.BASE)
            }
        }

        listOf(push, pull, pp0, pp1, load).forEach {
            it.buttonType = JFXButton.ButtonType.FLAT
            it.ripplerFill = Styles.ripple
            it.background = Background(BackgroundFill(Styles.green, CornerRadii(2.5), null))
            it.textFill = Styles.white
            it.text = it.text.toUpperCase()
        }

        push.setOnAction(runAlgorithm(Push::class.java))
        pull.setOnAction(runAlgorithm(Pull::class.java))
        pp0.setOnAction(runAlgorithm(PP0::class.java))
        pp1.setOnAction(runAlgorithm(PP1::class.java))
    }

    private fun runAlgorithm(algorithm: Class<out RumorAlgorithm>): (ActionEvent) -> Unit {
        val callback: (RumorAlgorithm.Action) -> Unit = {
            Platform.runLater {
                when (it) {
                    is Failure -> {
                        val edge = graphPane.edges.findEdge(it.edge.from, it.edge.to)
                        edge.activateError()
                        if (it.reversed) {
                            edge.to.activate()
                        } else {
                            edge.from.activate()
                        }
                    }
                    is Success -> {
                        val edge = graphPane.edges.findEdge(it.edge.from, it.edge.to)
                        edge.activateOk()
                        if (it.reversed) {
                            edge.to.activate()
                            edge.from.select()
                        } else {
                            edge.from.activate()
                            edge.to.select()
                        }
                    }
                    is Reset -> {
                        graphPane.circles.forEach { it.deactivate() }
                        graphPane.edges.forEach { it.deactivate() }
                    }
                    is Clear -> {
                        graphPane.circles.forEach { it.deactivate() }
                        graphPane.circles.forEach { it.deselect() }
                        graphPane.edges.forEach { it.deactivate() }
                    }
                    is NewRound -> {
                        rounds.text = "Actual rounds: ${it.round}"
                    }
                }
            }
        }
        return { ActionEvent ->
            Unit
            val selectedNodes = graphPane.graph.nodes.filter { it.selected }.count()
            if(selectedNodes == 1){
                task {
                    val instance = algorithm.getDeclaredConstructor().newInstance().apply {
                        animate = showAnimationBinding
                        sleep = sleepBinding
                        updateTask = callback
                    }
                    instance.run(graphPane.copyGraph(), graphPane.copyEdges())
                }
            }
        }
    }
}