package io.orbit.ui

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.animation.TranslateTransition
import javafx.application.Platform
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid
import org.kordamp.ikonli.javafx.FontIcon

/**
 * Created by Tyler Swann on Sunday January 21, 2018 at 17:34
 */

class MUITabBar: VBox
{
    private val tabBar = Pane()
    private val lineContainer = Pane()
    private val line = Pane()
    private var mouseX = 0.0
    private var draggingTab: MUITab? = null
    public val tabs: ObservableList<MUITab> = FXCollections.observableArrayList<MUITab>()

    constructor(): this(emptyArray())
    constructor(tabs: Array<MUITab>)
    {
        //TODO - fix remove animation
        this.tabs.addAll(tabs)
        this.tabs.forEach { addEventsToTab(it) }
        this.prefWidth = 200.0
        this.alignment = Pos.TOP_CENTER
        this.lineContainer.prefHeight = 2.0
        this.tabBar.children.addAll(this.tabs)
        this.tabBar.prefHeight = 45.0
        this.line.prefHeight = 2.0
        this.line.prefWidth = 150.0
        this.lineContainer.children.add(this.line)
        this.children.addAll(this.tabBar, this.lineContainer)
        this.tabBar.heightProperty().addListener({ _ -> this.getTabs().forEach { tab -> tab.prefHeight = this.tabBar.height }})
        this.lineContainer.heightProperty().addListener({_ -> this.line.prefHeight = this.lineContainer.height})
        this.styleClass.add("mui-tab-bar")
        this.line.styleClass.add("mui-tab-bar-line")
        this.tabBar.children.addListener(ListChangeListener { change ->
            while (change.next())
            {
                val added = change.addedSubList
                val removed = change.removed
                added.forEach {
                    if (it == added.last() && it is MUITab)
                    {
                        var removeListener: (() -> Unit) = {}
                        val updateLine: ((Observable) -> Unit) = {_ ->
                            Platform.runLater { transitionLineXTo(it.layoutX, it.width) }
                            removeListener()
                        }
                        removeListener = { it.widthProperty().removeListener(updateLine) }
                        it.widthProperty().addListener(updateLine)
                    }
                }
                removed.forEach {
                    if (it == removed.last() && it is MUITab && this.tabs.size > 0)
                    {
                        val removedIndex = change.list.indexOf(it)
                        val goToIndex = if (this.tabs.size < removedIndex) removedIndex - 1 else removedIndex + 1
                        Platform.runLater {
                            this.transitionLineXTo(this.tabs[goToIndex].layoutX, this.tabs[goToIndex].width)
                        }
                    }
                }
            }
        })
        if (this.tabs.size == 0)
            Platform.runLater { this.line.prefWidth = this.width }
        this.tabs.addListener(ListChangeListener { change ->
            var added = 0
            while (change.next())
            {
                change.addedSubList.forEach { tab ->
                    added++
                    addEventsToTab(tab)
                    this.tabBar.children.add(tab)
                    tab.didAddToParent()
                }
                change.removed.forEach {
                    this.tabBar.children.remove(it)
                }
            }
            this.getTabs().forEach { tab -> tab.prefHeight = this.tabBar.height }
            layoutTabs()
        })
        layoutTabs()
    }

    private fun transitionLineXTo(x: Double, width: Double)
    {
        val duration = Duration(150.0)
        val translateTransition = TranslateTransition(duration, this.line)
        translateTransition.toX = x
        translateTransition.fromX = this.line.translateX
        translateTransition.play()
        val widthTransition = Timeline(KeyFrame(duration, KeyValue(this.line.prefWidthProperty(), width)))
        widthTransition.play()
    }


    private fun layoutTabs() = this.layoutTabs(this.getTabs())
    private fun layoutTabs(tabs: ArrayList<out MUITab>)
    {
        var newX = 0.0
        Platform.runLater {
            tabs.forEach {
                it.layoutX = newX
                newX += it.width + 5.0
            }
        }
    }

    private fun getTabs(): ArrayList<MUITab>
    {
        val tabs = ArrayList<MUITab>()
        this.tabBar.children.forEach { child ->
            if (child is MUITab)
                tabs.add(child)
        }
        return tabs
    }

    private fun addEventsToTab(tab: MUITab)
    {
        tab.addEventHandler(MouseEvent.MOUSE_CLICKED, { _ ->
            if (this.draggingTab != null) return@addEventHandler
            this.transitionLineXTo(tab.layoutX, tab.width)
        })
        tab.addEventHandler(MouseEvent.MOUSE_PRESSED, {event -> this.mouseX = event.sceneX })
        tab.addEventHandler(MouseEvent.MOUSE_DRAGGED, {event ->
            if (this.draggingTab == null)
                this.draggingTab = tab
            tab.toFront()
            val deltaX = event.sceneX - this.mouseX
            tab.relocate(tab.layoutX + deltaX, tab.layoutY )
            this.mouseX = event.sceneX
        })
        tab.addEventHandler(MouseEvent.MOUSE_RELEASED, {
            if (this.draggingTab != null)
            {
                val sortedTabs = this.tabBar.children.sortedWith(compareBy { it.layoutX }).map { (it as MUITab) }
                this.layoutTabs(ArrayList(sortedTabs))
                Platform.runLater { this.transitionLineXTo(tab.layoutX, tab.width) }
                this.draggingTab = null
            }
        })
        val close = MUIMenuItem("Close")
        val closeOthers = MUIMenuItem("Close Others")
        val closeAll = MUIMenuItem("Close All")
        val closeAllButPinned = MUIMenuItem("Close All but Pinned")
        val contextMenu = MUIContextMenu(
                close,
                closeOthers,
                closeAll,
                closeAllButPinned
        )
        close.setOnAction { _ -> this.tabs.remove(tab) }
        closeAll.setOnAction { _ -> this.tabs.removeAll(this.tabs) }
        closeAllButPinned.setOnAction { _ -> println("TODO") }
        closeOthers.setOnAction { _ ->
            this.tabs.removeAll(this.tabs)
            this.tabs.add(tab)
        }
        tab.onCloseRequested = Runnable { this.tabs.remove(tab) }
        tab.contextMenu = contextMenu
    }
}