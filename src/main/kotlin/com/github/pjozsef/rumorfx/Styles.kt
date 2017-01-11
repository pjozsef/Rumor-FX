package com.github.pjozsef.rumorfx

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.c

class Styles: Stylesheet() {
    companion object{
        val darkgreen: Color = c("01611e")
        val green: Color = c("0f9d58")
        val active: Color = Color.YELLOWGREEN
        val ripple: Color = c("#f4f4f4")
        val white: Color = c("#fff")
        val red: Color = Color.DARKRED
        val background: Color = c("#fafafa")
        val backgroundDark: Color = c("#DCE6DC")
        val transparent: Color = c(0.97, 0.97, 0.97, 0.0)
    }
}