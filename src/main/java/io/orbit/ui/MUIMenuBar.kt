package io.orbit.ui

import com.sun.javafx.collections.TrackableObservableList
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox


/**
 * Created by Tyler Swann on Friday February 02, 2018 at 15:21
 */
class MUIMenuBar: HBox
{
    private val rightContainer = HBox()
    private val leftContainer = HBox()

    public var sidePadding = 2.0
        set(value) {
            this.leftContainer.padding = Insets(0.0, 0.0, 0.0, value)
            this.rightContainer.padding = Insets(0.0, value, 0.0, 0.0)
        }
    public var rightPadding = 2.0
        set(value) { this.rightContainer.padding = Insets(0.0, value, 0.0, 0.0) }
    public var leftPadding = 2.0
        set(value) { this.leftContainer.padding = Insets(0.0, 0.0, 0.0, value) }

    private val rightItems: ObservableList<MUIMenuButton>
    public fun rightItemsProperty(): ObservableList<MUIMenuButton> = rightItems
    public fun getRightItems(): ObservableList<MUIMenuButton> = rightItems
    private val leftItems: ObservableList<MUIMenuButton>
    public fun leftItemsProperty(): ObservableList<MUIMenuButton> = leftItems
    public fun getLeftItems(): ObservableList<MUIMenuButton> = leftItems

    constructor(): this(emptyArray(), emptyArray())
    constructor(leftButtons: Array<MUIMenuButton>, rightButtons: Array<MUIMenuButton>)
    {
        val spacing = 10.0
        val padding = 2.0
        this.prefHeight = 25.0
        this.prefWidth = 200.0
        this.alignment = Pos.CENTER
        this.spacing = 0.0
        this.leftContainer.alignment = Pos.CENTER_LEFT
        this.rightContainer.alignment = Pos.CENTER_RIGHT
        this.leftContainer.padding = Insets(0.0, 0.0, 0.0, padding)
        this.rightContainer.padding = Insets(0.0, padding, 0.0, 0.0)
        this.leftContainer.spacing = spacing
        this.rightContainer.spacing = spacing
        this.leftContainer.children.addAll(leftButtons)
        this.rightContainer.children.addAll(rightButtons)
        this.children.addAll(this.leftContainer, this.rightContainer)
        this.heightProperty().addListener({_ ->
            this.rightContainer.prefHeight = this.height
            this.leftContainer.prefHeight = this.height
        })
        this.widthProperty().addListener({_ ->
            this.rightContainer.prefWidth = (this.width / 2)
            this.leftContainer.prefWidth = (this.width / 2)
        })
    }

    init
    {
        this.styleClass.add("mui-menu-bar")
        this.rightItems = object: TrackableObservableList<MUIMenuButton>(){
            override fun onChanged(change: ListChangeListener.Change<MUIMenuButton>?) {
                if (change == null)
                    return
                while (change.next())
                {
                    for (removedItem in change.removed)
                        rightContainer.children.remove(removedItem)
                    for(added in change.addedSubList)
                        rightContainer.children.add(added)
                }
            }
        }
        this.leftItems = object: TrackableObservableList<MUIMenuButton>(){
            override fun onChanged(change: ListChangeListener.Change<MUIMenuButton>?) {
                if (change == null)
                    return
                while (change.next())
                {
                    for (removedItem in change.removed)
                        leftContainer.children.remove(removedItem)
                    for(added in change.addedSubList)
                        leftContainer.children.add(added)
                }
            }
        }
    }
}