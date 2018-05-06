package io.orbit.ui

import javafx.application.Platform
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem

/**
 * Created by Tyler Swann on Sunday January 07, 2018 at 09:53
 */
class MUIContextMenu: ContextMenu
{
    constructor()
    constructor(vararg items: MenuItem): super(*items)

    init
    {
        this.styleClass.add("mui-context-menu")
        this.setOnShowing { Platform.runLater { updateWidth(this.items.toTypedArray(), (this.width + 10.0)) } }
    }

    private fun updateWidth(items: Array<MenuItem>, width: Double)
    {
        items.forEach { item ->
            if (item is MUIMenu)
            {
                item.setPrefWidth(width)
                updateWidth(item.items.toTypedArray(), width)
            }
            else if (item is MUIMenuItem)
                item.setPrefWidth(width)
            //else if (item is Menu)

        }
    }
}