package io.orbit.ui

import javafx.animation.ScaleTransition
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.input.MouseEvent
import javafx.util.Duration
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid
import org.kordamp.ikonli.javafx.FontIcon

/**
 * Created by Tyler Swann on Sunday January 21, 2018 at 17:35
 */
class MUITab: MUIButton
{
    private val closeIcon = FontIcon()
    public var onCloseRequested: Runnable = Runnable {  }

    constructor(text: String): super(text)
    {
        this.minWidth = 150.0
        this.maxWidth = 220.0
        this.styleClass.add("mui-tab")
        this.closeIcon.styleClass.add("mui-tab-close")
        this.closeIcon.iconCode = FontAwesomeSolid.TIMES
        this.closeIcon.toFront()
        this.graphic = this.closeIcon
        this.contentDisplay = ContentDisplay.RIGHT
        this.alignment = Pos.CENTER
        this.padding = Insets(0.0, 20.0, 0.0, 20.0)
        this.closeIcon.scaleX = 0.0
        this.closeIcon.scaleY = 0.0
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, { setCloseButtonOpen(true) })
        this.addEventHandler(MouseEvent.MOUSE_EXITED, { setCloseButtonOpen(false) })
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, { event ->
            if (this.closeIcon.boundsInParent.contains(event.x, event.y))
                this.onCloseRequested.run()
        })
    }

    internal fun didAddToParent()
    {
        Platform.runLater {
            this.closeIcon.translateX = Math.abs((this.closeIcon.boundsInParent.minX + this.closeIcon.boundsInLocal.width) - this.width)
        }
    }

    private fun setCloseButtonOpen(open: Boolean)
    {
        val transition = ScaleTransition(Duration(150.0), this.closeIcon)
        transition.fromX = if (open) 0.0 else 1.0
        transition.fromY = if (open) 0.0 else 1.0
        transition.toX = if (open) 1.0 else 0.0
        transition.toY = if (open) 1.0 else 0.0
        transition.play()
    }

    public override fun setWidth(value: Double) = super.setWidth(value)
    public override fun setHeight(value: Double) = super.setHeight(value)
}