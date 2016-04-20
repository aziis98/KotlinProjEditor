package com.aziis98.game1

import org.newdawn.slick.*
import javax.swing.JFrame

// Copyright 2016 Antonio De Lucreziis

class Main() : BasicGame("ProjEditor") {

    override fun init(container: GameContainer) {

    }

    override fun render(container: GameContainer, g: Graphics) {
        g.background = Color.white
    }

    override fun update(container: GameContainer, delta: Int) {
        if (!jframe.isVisible) System.exit(0)


    }

}

var jframe = JFrame("ProjEditor")

fun main(args: Array<String>) {

    val canvasgc = CanvasGameContainer(Main())
    canvasgc.container.alwaysRender = false
    canvasgc.container.setForceExit(true)

    jframe = JFrame("ProjEditor")

    with(jframe) {
        setSize(1200, 900)
        setLocationRelativeTo(null)
        isResizable = true
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

        contentPane.add(canvasgc)
        isVisible = true
    }

    canvasgc.start()

}