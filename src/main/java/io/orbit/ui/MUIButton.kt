package io.orbit.ui

import com.sun.javafx.css.converters.PaintConverter
import javafx.animation.FadeTransition
import javafx.animation.ScaleTransition
import javafx.application.Platform
import javafx.css.*
import javafx.scene.control.Button
import javafx.scene.control.Control
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.util.*


/**
 * Created by Tyler Swann on Saturday February 03, 2018 at 18:18
 */
@Deprecated("No Longer Used")
public open class MUIButton: Button
{
    private var ripples = ArrayList<Ripple>()
    public var rippleSpeed = 300.0
    public var fadeSpeed = 400.0

    constructor(): this("BUTTON")
    constructor(text: String): super(text)
    {
        addListeners()
        Platform.runLater {
            val mask = Rectangle(this.width, this.height)
            this.widthProperty().addListener { _-> mask.width = this.width }
            this.heightProperty().addListener { _-> mask.height = this.height }
            this.clip = mask
        }
        this.styleClass.add("mui-button")
    }

    private fun addListeners()
    {
        this.textFill = this.textColor.get()
        this.textColorProperty().addListener { _-> this.textFill = this.textColor.get() }
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, { event ->
            val ripple = Ripple(this)
            this.children.add(ripple)
            this.ripples.add(ripple)
            ripple.playScaleAnimation(event.x, event.y)
        })
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, {
            val ripples = ArrayList<Ripple>(this.ripples)
            this.ripples.clear()
            ripples.forEach {
                it.animateOpacity(Runnable {
                    this.children.remove(it)
                })
            }
        })
    }

    inner class Ripple: Circle
    {
        constructor(button: MUIButton): this(button.width, button.height)
        constructor(width: Double, height: Double)
        {
            this.fill = rippleColor.get()
            this.isPickOnBounds = false
            Platform.runLater {
                this.radius = if (width > height) width else height
                this.scaleY = 0.0
                this.scaleX = 0.0
            }
        }
        internal fun playScaleAnimation(x: Double, y: Double)
        {
            this.toFront()
            this.centerX = x
            this.centerY = y
            val scaleTransition = ScaleTransition(Duration(rippleSpeed), this)
            scaleTransition.fromX = 0.0
            scaleTransition.fromY = 0.0
            scaleTransition.toX = 1.2
            scaleTransition.toY = 1.2
            scaleTransition.play()
        }
        internal fun animateOpacity(completion: Runnable)
        {
            val opacityTransition = FadeTransition(Duration(fadeSpeed), this)
            opacityTransition.fromValue = 1.0
            opacityTransition.toValue = 0.0
            opacityTransition.play()
            opacityTransition.setOnFinished { completion.run() }
        }
    }


    /**************************************************************************
     *                                                                        *
     *                              Styling                                   *
     *                                                                        *
     **************************************************************************/

    public override fun getControlCssMetaData(): List<CssMetaData<out Styleable, *>> = STYLEABLES

    /********************************
     *                              *
     *         Ripple Color         *
     *                              *
     ********************************/
    private val rippleColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(RIPPLE_COLOR, this, "rippleColor")
    public fun rippleColorProperty(): StyleableObjectProperty<Paint> = rippleColor
    public fun getRippleColor(): Paint = rippleColor.get()
    public fun setRippleColor(value: Paint) = rippleColor.set(value)

    /********************************
     *                              *
     *          Text color          *
     *                              *
     ********************************/
    private val textColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(TEXT_COLOR, this, "textColor", Color.BLACK)
    public fun textColorProperty(): StyleableObjectProperty<Paint> = textColor
    public fun getTextColor(): Paint = textColor.get()
    public fun setTextColor(value: Paint) = textColor.set(value)

    internal companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val RIPPLE_COLOR = object: CssMetaData<MUIButton, Paint>("-fx-ripple-color", PaintConverter.getInstance(), Color.color(0.0, 0.0, 0.0, 0.3)) {
            override fun isSettable(styleable: MUIButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIButton?): StyleableProperty<Paint> = styleable?.rippleColorProperty()!!
        }

        internal val TEXT_COLOR = object: CssMetaData<MUIButton, Paint>("-fx-text-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUIButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIButton?): StyleableProperty<Paint> = styleable?.textColorProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(Control.getClassCssMetaData().size, { index -> Control.getClassCssMetaData()[index]})
            Collections.addAll(styleables, RIPPLE_COLOR, TEXT_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
}