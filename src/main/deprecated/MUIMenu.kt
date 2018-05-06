package io.orbit.ui

import javafx.geometry.Pos
import javafx.scene.control.Menu
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.MenuItem

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 11:25
 */
class MUIMenu: Menu
{
    private val prefHeightProperty: DoubleProperty
    private val prefWidthProperty: DoubleProperty
    public var itemText = ""
        private set

    constructor(text: String, vararg items: MenuItem)
    {
        this.itemText = text
        val button = MUIButton(text)
        button.alignment = Pos.CENTER_LEFT
        button.prefWidth = 100.0
        button.prefHeight = 40.0
        this.prefWidthProperty = SimpleDoubleProperty()
        this.prefHeightProperty = SimpleDoubleProperty()
        this.prefHeightProperty.addListener { _-> button.prefHeight = this.prefHeightProperty.get() }
        this.prefWidthProperty.addListener { _-> button.prefWidth = this.prefWidthProperty.get() }
        this.graphic = button
        this.items.addAll(items)
        this.textProperty().addListener { _->
            if (this.text != null)
            {
                button.text = this.text
                this.itemText = this.text
                this.text = null
            }
        }
    }

    public fun prefWidthProperty(): SimpleDoubleProperty = this.prefWidthProperty()
    public fun getPrefWidth(): Double = this.prefWidthProperty.get()
    public fun setPrefWidth(value: Double) = this.prefWidthProperty.set(value)
    public fun prefHeightProperty(): SimpleDoubleProperty = this.prefHeightProperty()
    public fun getPrefHeight(): Double = this.prefWidthProperty.get()
    public fun setPrefHeight(value: Double) = this.prefWidthProperty.set(value)

    init
    {
        this.styleClass.add("mui-menu")
    }
}