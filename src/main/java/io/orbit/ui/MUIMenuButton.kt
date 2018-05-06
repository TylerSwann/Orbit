package io.orbit.ui

import com.sun.javafx.css.converters.PaintConverter
import javafx.application.Platform
import javafx.css.*
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Tyler Swann on Friday February 02, 2018 at 15:23
 */
public open class MUIMenuButton: MUIButton
{
    private var iconView: FontIcon? = null
    public var iconOpacity: Double
        set(value) { iconView?.opacity = value }
        get() = iconView?.opacity ?: -1.0
    public var iconXOffset = 0.0
        set(value) {
            val translateX = this.iconView?.translateX ?: 0.0
            this.iconView?.translateX = (translateX + value)
        }
    public val menu = MUIContextMenu()
    public var icon: Ikon? = null
        set(value) {  this.iconView = FontIcon(value)  }
    public var iconScale = 1.0
        set(value) {
            this.iconView?.scaleX = value
            this.iconView?.scaleY = value
        }

    constructor(): this("BUTTON")

    constructor(text: String): super(text)

    constructor(icon: Ikon): super("")
    {
        this.text = null
        this.icon = icon
        this.graphic = this.iconView
    }

    constructor(icon: Ikon, text: String, display: ContentDisplay): super(text)
    {
        this.icon = icon
        this.contentDisplay = display
        this.graphic = this.iconView
    }

    init
    {
        this.alignment = Pos.CENTER
        this.minWidth = 50.0
        this.styleClass.add("mui-menu-button")
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, { _ ->
            if(this.menu.items.size > 0)
                this.menu.show(this)
        })
        Platform.runLater { addStylingEvents() }

    }

    /**
     * Listen for changes to the styleable properties, then apply them to the element
     */
    private fun addStylingEvents()
    {
        this.textFill = getTextColor()
        this.iconView?.fill = getIconColor()
        this.textColorProperty().addListener { _-> this.textFill = getTextColor() }
        this.iconColorProperty().addListener { _-> this.iconView?.fill = getIconColor() }
    }


    /**************************************************************************
     *                                                                        *
     *                              Styling                                   *
     *                                                                        *
     **************************************************************************/

    /********************************
     *                              *
     *          Icon color          *
     *                              *
     ********************************/
    private val iconColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(ICON_COLOR, this, "iconColor", Color.BLACK)
    public fun iconColorProperty(): StyleableObjectProperty<Paint> = iconColor
    public fun getIconColor(): Paint = iconColor.get()
    public fun setIconColor(value: Paint) = iconColor.set(value)


    public override fun getControlCssMetaData(): List<CssMetaData<out Styleable, *>> = STYLEABLES

    public companion object StyleableProperties
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val ICON_COLOR = object: CssMetaData<MUIMenuButton, Paint>("-fx-icon-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUIMenuButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIMenuButton?): StyleableProperty<Paint> = styleable?.iconColorProperty()!!
        }

        init
        {
            val inheritedStyleables = ArrayList<CssMetaData<out Styleable, *>>()
            inheritedStyleables.addAll(Control.getClassCssMetaData())
            inheritedStyleables.addAll(Companion.STYLEABLES)
            val styleables = MutableList(inheritedStyleables.size, {index -> inheritedStyleables[index]})
            Collections.addAll(styleables, ICON_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
}