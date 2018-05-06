package io.orbit.util

import javafx.animation.PauseTransition
import javafx.scene.Node
import javafx.util.Duration
import javax.swing.Timer

/**
 * Created by Tyler Swann on Sunday February 04, 2018 at 14:29
 */



inline fun repeat(crossinline action: () -> Unit)
{
    Timer(1000, { action() }).start()
}

fun wait(millis: Double, action: () -> Unit)
{
    val pause = PauseTransition(Duration(millis))
    pause.setOnFinished { action() }
    pause.play()
}
fun frameOfNode(node: Node): Frame
{
    val bounds = node.boundsInLocal
    val screenBounds = node.localToScreen(bounds)
    val x = screenBounds.minX
    val y = screenBounds.minY
    val width = screenBounds.width
    val height = screenBounds.height
    return Frame(x, y, width, height)
}



