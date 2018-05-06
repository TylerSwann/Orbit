package io.orbit.ui

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid

/**
 * Created by Tyler Swann on Thursday February 08, 2018 at 15:09
 */
class MUIPagination: HBox
{
    private val items = FXCollections.observableArrayList<Pane>()
    private val rightButton: MUIMenuButton
    private val leftButton: MUIMenuButton
    private val contentContainer: Pane
    public fun getItems(): ObservableList<Pane> = items

    constructor(vararg  items: Pane)
    {
        items.forEach { this.items.add(it) }
        this.contentContainer = Pane()
        this.rightButton = MUIMenuButton(FontAwesomeSolid.ANGLE_RIGHT)
        this.leftButton = MUIMenuButton(FontAwesomeSolid.ANGLE_LEFT)
        this.alignment = Pos.CENTER
        Platform.runLater {
            this.rightButton.setPrefSize(this.height, this.height)
            this.leftButton.setPrefSize(this.height, this.height)
            this.contentContainer.prefHeight = this.height
            this.contentContainer.prefWidth = if (this.items.size > 1) this.items[0].width else 200.0
            this.rightButton.opacity = 0.0
            this.leftButton.opacity = 0.0
            this.contentContainer.children.addAll(this.items)
            this.layoutContent()
        }
        if (this.items.size > 1)
            this.rightButton.opacity = 1.0

    }

    private fun layoutContent()
    {
        var xOffset = 0.0
        this.items.forEach { item ->
            item.layoutX = xOffset
            xOffset += this.contentContainer.width
        }
    }
}