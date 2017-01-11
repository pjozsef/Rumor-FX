package com.github.pjozsef.rumorfx

import com.github.pjozsef.rumorfx.view.MainView
import javafx.application.Application
import tornadofx.App

fun main(args: Array<String>) {
    Application.launch(RumorFX::class.java)
}

class RumorFX : App(MainView::class)