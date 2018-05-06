package io.orbit.ui

import io.orbit.util.wait
import javafx.application.Platform
import javafx.scene.control.ContentDisplay
import javafx.scene.input.MouseEvent
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid

/**
 * Created by Tyler Swann on Thursday February 08, 2018 at 17:34
 */
public class MUISubMenu: MUIMenuButton
{
    private val subMenu: MUIContextMenu
    private var mouseIsOver = false

    constructor(): this("SubMenu")
    constructor(text: String, vararg items: MUIMenuItem): super(FontAwesomeSolid.ANGLE_RIGHT, text, ContentDisplay.RIGHT)
    {
        this.subMenu = MUIContextMenu(*items)
        this.prefWidth = 150.0
        this.prefHeight = 40.0
        this.iconXOffset = 30.0
        this.subMenu.isAutoHide = true
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, { this.mouseIsOver = true })
        this.subMenu.addEventHandler(MouseEvent.MOUSE_EXITED, {
            this.mouseIsOver = false
            wait(500.0, {
                println(this.subMenu.isShowing)
                if(this.subMenu.isShowing)
                    this.subMenu.hide()
            })
        })
        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, { _->
            Platform.runLater {
                this.subMenu.show(this, this.width, 0.0)
            }
        })
        this.subMenu.addEventFilter(MouseEvent.MOUSE_CLICKED, {_->
            if (this.scene != null && this.scene.window != null && this.scene.window is MUIContextMenu)
                (this.scene.window as MUIContextMenu).hide()
        })
    }
}