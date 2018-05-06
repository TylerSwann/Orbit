package io.orbit.ui

import javafx.beans.property.SimpleBooleanProperty
import javafx.css.PseudoClass
import javafx.scene.Node
import javafx.scene.control.TreeItem

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 16:56
 */
class MUITreeItem<T>: TreeItem<T>
{
    public var userData: Any? = null
    private val highlightedProperty = SimpleBooleanProperty(false)
    public fun highlightedProperty(): SimpleBooleanProperty = highlightedProperty
    public var isHighlighted: Boolean
        get() = highlightedProperty.value
        set(value) = highlightedProperty.set(value)

    constructor()
    constructor(value: T): super(value)
    constructor(value: T, graphic: Node): super(value, graphic)

    init
    {
        this.highlightedProperty.addListener { _ ->
            this.graphicProperty()
        }
    }

    companion object
    {
        private val HIGHLIGHTED = PseudoClass.getPseudoClass("highlighted")
    }
}