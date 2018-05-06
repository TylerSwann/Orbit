package io.orbit.ui

import com.sun.javafx.css.converters.FontConverter
import com.sun.javafx.css.converters.PaintConverter
import javafx.application.Platform
import javafx.css.*
import javafx.geometry.Insets
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import java.util.*
import java.util.function.Consumer
import javax.swing.Timer

/**
 * Created by Tyler Swann on Saturday January 20, 2018 at 15:54
 */
class MUITerminalPane: ScrollPane
{
    private var currentLine: TerminalLine? = null
    private var input = ""
    private var recordInput = false
    private var onInputCompletion: Consumer<String> = Consumer {}
    private val body = VBox()

    constructor()
    {
        addStyleListeners()
        this.body.cursor = Cursor.TEXT
    }

    init
    {
        this.isFitToWidth = true
        this.content = this.body
        this.styleClass.add("terminal")
        val pad = 5.0
        this.body.padding = Insets(pad, pad, pad, pad)
        val keyEvent: Consumer<KeyEvent> =  Consumer { event ->
            if (!recordInput || !this.isFocused)
                return@Consumer
            if (event.code == KeyCode.ENTER)
            {
                if (this.input == "")
                    this.addBlankLine()
                else
                {
                    this.onInputCompletion.accept(this.input)
                    this.recordInput = false
                    this.input = ""
                }
            }
            else
                this.updateInput(event.text)
        }
        if (this.scene != null)
            this.scene.addEventHandler(KeyEvent.KEY_RELEASED, { keyEvent.accept(it) })
        else
        {
            this.sceneProperty().addListener({_ ->
                if (this.scene != null)
                    this.scene.addEventHandler(KeyEvent.KEY_RELEASED, { keyEvent.accept(it) })
            })
        }
        this.focusedProperty().addListener { _->
            if (this.isFocused)
                this.currentLine?.focus()
            else
                this.currentLine?.unfocus()
        }
        this.heightProperty().addListener { _-> this.body.prefHeight = this.height }
        Platform.runLater { this.body.prefHeight = this.height }
    }

    private fun addStyleListeners()
    {
        val applyStyle: (() -> Unit) = {
            this.body.children.forEach {
                if (it is Label)
                {
                    it.font = this.getFont()
                    it.textFill = this.getFontColor()
                }
                else if (it is TerminalLine)
                {
                    it.line.font = this.getFont()
                    it.line.textFill = this.getFontColor()
                    it.caret.background = Background(BackgroundFill(this.getCaretColor(), CornerRadii.EMPTY, Insets.EMPTY))
                }
            }
            if (this.background != null && this.background.fills != null && this.background.fills.size > 0)
            {
                val fills = this.background.fills.toTypedArray()
                this.body.background = Background(*fills)
            }
            else
            {
                val fill = BackgroundFill(DEFAULT_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)
                this.background = Background(fill)
                this.body.background = Background(fill)
            }
        }
        applyStyle()
        this.fontColorProperty().addListener { _-> applyStyle() }
        this.caretColorProperty().addListener { _-> applyStyle()  }
        this.fontColorProperty().addListener { _-> applyStyle()  }
        this.backgroundProperty().addListener { _-> applyStyle()  }
    }

    private fun addBlankLine()
    {
        val text = this.currentLine?.line?.text ?: return
        this.currentLine?.unfocus()
        this.input(text, this.onInputCompletion)
    }
    private fun updateInput(text: String)
    {
        this.input += text
        this.currentLine?.appendText(text)
    }

    public fun print(text: String)
    {
        this.body.children.add(newLabel(text))
    }


    public fun input(text: String, completion: Consumer<String>)
    {
        this.currentLine?.unfocus()
        val previousText = this.currentLine?.line?.text
        this.body.children.remove(this.currentLine)
        this.body.children.add(newLabel(previousText))
        this.onInputCompletion = completion
        val line = TerminalLine(text, this.isFocused)
        this.currentLine = line
        this.body.children.add(this.currentLine)
        this.recordInput = true
    }

    private fun newLabel(text: String?): Label
    {
        val label = Label(text)
        label.font = this.getFont()
        label.textFill = this.getFontColor()
        return label
    }

    private inner class TerminalLine: HBox
    {
        internal val line: Label
        internal val caret = Pane()
        private val timer: Timer
        private var focusing: Boolean

        constructor(text: String, focused: Boolean)
        {
            this.line = newLabel(text)
            this.focusing = focused
            this.caret.setPrefSize(10.0, 18.0)
            this.line.text = text
            this.caret.styleClass.add("caret")
            this.caret.background = Background(BackgroundFill(getCaretColor(), CornerRadii.EMPTY, Insets.EMPTY))
            this.children.addAll(this.line, this.caret)
            this.timer = Timer(700, { this.caret.opacity = if (this.caret.opacity == 1.0) 0.0 else 1.0 })
            if (this.focusing)
                this.focus()
            else
                this.unfocus()
        }

        internal fun appendText(text: String)
        {
            this.line.text += text
        }

        internal fun focus()
        {
            this.caret.opacity = 1.0
            this.focusing = true
            if (this.timer.isRunning)
                return
            this.timer.start()
        }
        internal fun unfocus()
        {
            this.caret.opacity = 0.0
            this.focusing = false
            if (!this.timer.isRunning)
                return
            this.timer.stop()
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
     *             SerializableFont             *
     *                              *
     ********************************/

    private val font: StyleableObjectProperty<Font> = SimpleStyleableObjectProperty(FONT, this, "font", DEFAULT_FONT)
    public fun fontProperty(): StyleableObjectProperty<Font> = font
    public fun getFont(): Font = font.get()
    public fun setFont(value: Font) = font.set(value)

    /********************************
     *                              *
     *          SerializableFont Color          *
     *                              *
     ********************************/

    private val fontColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(FONT_COLOR, this, "fontColor", DEFAULT_FONT_COLOR)
    public fun fontColorProperty(): StyleableObjectProperty<Paint> = fontColor
    public fun getFontColor(): Paint = fontColor.get()
    public fun setFontColor(value: Paint) = fontColor.set(value)

    /********************************
     *                              *
     *         Caret Color          *
     *                              *
     ********************************/

    private val caretColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(CARET_COLOR, this, "caretColor", Color.BLACK)
    public fun caretColorProperty(): StyleableObjectProperty<Paint> = caretColor
    public fun getCaretColor(): Paint = caretColor.get()
    public fun setCaretColor(value: Paint) = caretColor.set(value)

    private companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val DEFAULT_BACKGROUND_COLOR = Color.color(0.91373,  0.91373,  0.91373)
        internal val DEFAULT_FONT_COLOR = Color.color(0.15686,  0.15686,  0.15686, 1.0)
        internal val DEFAULT_FONT = Font("Monospaced", 15.0)

        internal val CARET_COLOR = object: CssMetaData<MUITerminalPane, Paint>("-fx-caret-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUITerminalPane?): Boolean = true
            override fun getStyleableProperty(styleable: MUITerminalPane?): StyleableProperty<Paint> = styleable?.caretColorProperty()!!
        }

        internal val FONT_COLOR = object: CssMetaData<MUITerminalPane, Paint>("-fx-font-color", PaintConverter.getInstance(), DEFAULT_FONT_COLOR) {
            override fun isSettable(styleable: MUITerminalPane?): Boolean = true
            override fun getStyleableProperty(styleable: MUITerminalPane?): StyleableProperty<Paint> = styleable?.fontColorProperty()!!
        }

        internal val FONT = object: CssMetaData<MUITerminalPane, Font>("-fx-font", FontConverter.getInstance(), DEFAULT_FONT) {
            override fun isSettable(styleable: MUITerminalPane?): Boolean = true
            override fun getStyleableProperty(styleable: MUITerminalPane?): StyleableProperty<Font> = styleable?.fontProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(ScrollPane.getClassCssMetaData().size, { index -> ScrollPane.getClassCssMetaData()[index]})
            Collections.addAll(styleables, FONT_COLOR, FONT, CARET_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
    
    /*


    public override fun getControlCssMetaData(): List<CssMetaData<out Styleable, *>> = STYLEABLES

    /**
     * Ripple Color
     */
    private val rippleColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(RIPPLE_COLOR, this, "rippleColor")
    public fun rippleColorProperty(): StyleableObjectProperty<Paint> = rippleColor
    public fun getRippleColor(): Paint = rippleColor.get()
    public fun setRippleColor(value: Paint) = rippleColor.set(value)

    internal companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val RIPPLE_COLOR = object: CssMetaData<MUIButton, Paint>("-fx-ripple-color", PaintConverter.getInstance(), Color.color(0.0, 0.0, 0.0, 0.3)) {
            override fun isSettable(styleable: MUIButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIButton?): StyleableProperty<Paint> = styleable?.rippleColorProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(Control.getClassCssMetaData().size, { index -> Control.getClassCssMetaData()[index]})
            Collections.addAll(styleables, RIPPLE_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
    * */
}