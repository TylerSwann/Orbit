package io.orbit.ui

import javafx.application.Platform
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Created by Tyler Swann on Saturday January 06, 2018 at 16:54
 */
class MUITreeTableView<T>: TreeView<T>
{
    public var onItemDoubleClicked: Consumer<MUITreeItem<T>> = Consumer { _ -> }
    public var onItemClicked: Consumer<MUITreeItem<T>> = Consumer { _->  }
    public var onItemRightClicked: BiConsumer<MUITreeItem<T>, MouseEvent> = BiConsumer { _,_-> }
    private var selectedItem: MUITreeItem<T>? = null

    constructor(): this(null)
    constructor(item: MUITreeItem<T>?): super(item)
    {
        Platform.runLater { registerListeners() }
    }

    private fun registerListeners()
    {
        this.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue is MUITreeItem<T>)
                this.selectedItem = newValue
        }
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, {event ->
            val selectedItem = this.selectedItem ?: return@addEventHandler
            if (event.button == MouseButton.PRIMARY && event.clickCount == 2)
                this.onItemDoubleClicked.accept(selectedItem)
            if (event.button == MouseButton.PRIMARY)
                this.onItemClicked.accept(selectedItem)
            if (event.button == MouseButton.SECONDARY)
                this.onItemRightClicked.accept(selectedItem, event)
        })
    }
}















