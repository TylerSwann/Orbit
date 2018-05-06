package io.orbit.ui

import com.jfoenix.controls.JFXTabPane

/**
 * Created by Tyler Swann on Wednesday February 07, 2018 at 15:40
 */
class MUITabPane: JFXTabPane
{
    constructor()

    init
    {
        this.styleClass.add("mui-tab-pane")
    }
}