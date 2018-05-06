package io.orbit.ui

import com.sun.javafx.css.converters.PaintConverter
import io.orbit.util.DoubleConverter
import io.orbit.util.frameOfNode
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.css.*
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.PopupControl
import javafx.scene.effect.DropShadow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.stage.WindowEvent
import java.util.*

/**
 * Created by Tyler Swann on Thursday February 08, 2018 at 17:33
 */
public class MUIContextMenu: PopupControl
{
    private val root = AnchorPane()
    private val container = VBox()
    public val items: ObservableList<MUIMenuButton> = FXCollections.observableArrayList()

    constructor(vararg items: MUIMenuButton)
    {
        this.isAutoHide = true
        this.items.addAll(items)
        AnchorPane.setTopAnchor(this.container, 0.0)
        AnchorPane.setBottomAnchor(this.container, 0.0)
        AnchorPane.setLeftAnchor(this.container, 0.0)
        AnchorPane.setRightAnchor(this.container, 0.0)
        this.scene.root = this.root
        this.root.setPrefSize(150.0, 500.0)
        this.width = 150.0
        this.height = 500.0
        this.root.children.add(this.container)
        this.container.children.addAll(this.items)
        this.root.styleClass.add("mui-context-menu")
        val shadow = DropShadow()
        shadow.spread = 0.0
        shadow.offsetY = 2.0
        shadow.offsetX = 1.5
        shadow.color = Color.GREY
        this.root.effect = shadow
        registerListeners()
    }

    public fun show(owner: Node)
    {
        val frame = frameOfNode(owner)
        val x = frame.x + (frame.width / 2.0)
        val y = frame.y + (frame.height / 2.0)
        super.show(owner, x, y)
    }

    public override fun show(ownerNode: Node?, anchorX: Double, anchorY: Double)
    {
        adaptSize()
        val owner = ownerNode ?: return
        val offsetY = (owner.scene.window.height - owner.scene.height)
        val x = owner.scene.window.x + anchorX + 8.0
        val y = owner.scene.window.y + anchorY + offsetY + 7.0
        super.show(owner, x, y)
    }

    private fun registerListeners()
    {
        this.root.background = Background(BackgroundFill(this.getBackgroundColor(), CornerRadii(this.getBackgroundRadius()), Insets.EMPTY))
        this.backgroundColorProperty().addListener { _-> this.root.background = Background(BackgroundFill(this.getBackgroundColor(), CornerRadii(this.getBackgroundRadius()), Insets.EMPTY)) }
        this.backgroundRadiusProperty().addListener { _-> this.root.background = Background(BackgroundFill(this.getBackgroundColor(), CornerRadii(this.getBackgroundRadius()), Insets.EMPTY)) }
        this.items.addListener(ListChangeListener { change ->
            while (change.next())
            {
                val added = change.addedSubList
                val removed = change.removed
                added.forEach { this.container.children.add(it) }
                removed.forEach { this.container.children.remove(it) }
            }
            adaptSize()
        })
    }


    private fun adaptSize()
    {
        var prefHeight = 0.0
        var prefWidth = if (items.size > 0) items[0].prefWidth else 150.0
        this.items.forEach { prefHeight += it.prefHeight }
        this.root.setPrefSize(prefWidth, prefHeight)
        this.width = prefWidth
        this.height = prefHeight
    }

    /**************************************************************************
     *                                                                        *
     *                              Styling                                   *
     *                                                                        *
     **************************************************************************/

    public override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> = STYLEABLES

    /********************************
     *                              *
     *       Background color       *
     *                              *
     ********************************/

    private val backgroundColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(BACKGROUND_COLOR, this, "backgroundColor", Color.WHITE)
    public fun backgroundColorProperty(): StyleableObjectProperty<Paint> = backgroundColor
    public fun getBackgroundColor(): Paint = backgroundColor.get()
    public fun setBackgroundColor(value: Paint) = backgroundColor.set(value)

    /********************************
     *                              *
     *       Background radius      *
     *                              *
     ********************************/

    private val backgroundRadius: StyleableObjectProperty<Double> = SimpleStyleableObjectProperty(BACKGROUND_RADIUS, this, "backgroundRadius", 2.0)
    public fun backgroundRadiusProperty(): StyleableObjectProperty<Double> = backgroundRadius
    public fun getBackgroundRadius(): Double = backgroundRadius.get()
    public fun setBackgroundRadius(value: Double) = backgroundRadius.set(value)

    private companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val BACKGROUND_COLOR = object: CssMetaData<MUIContextMenu, Paint>("-fx-background-color", PaintConverter.getInstance(), Color.WHITE) {
            override fun isSettable(styleable: MUIContextMenu?): Boolean = true
            override fun getStyleableProperty(styleable: MUIContextMenu?): StyleableProperty<Paint> = styleable?.backgroundColorProperty()!!
        }

        internal val BACKGROUND_RADIUS = object: CssMetaData<MUIContextMenu, Double>("-fx-background-radius", DoubleConverter.getInstance(), 2.0) {
            override fun isSettable(styleable: MUIContextMenu?): Boolean = true
            override fun getStyleableProperty(styleable: MUIContextMenu?): StyleableProperty<Double> = styleable?.backgroundRadiusProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(PopupControl.getClassCssMetaData().size, { index -> PopupControl.getClassCssMetaData()[index]})
            Collections.addAll(styleables, BACKGROUND_COLOR, BACKGROUND_RADIUS)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }

}