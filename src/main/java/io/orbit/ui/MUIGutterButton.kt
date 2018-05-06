package io.orbit.ui


import com.sun.javafx.css.converters.FontConverter
import com.sun.javafx.css.converters.PaintConverter
import javafx.css.*
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid
import org.kordamp.ikonli.javafx.FontIcon
import java.util.*

/**
 * Created by Tyler Swann on Sunday February 11, 2018 at 12:10
 */
class MUIGutterButton: HBox
{
    private val iconView: FontIcon
    private val label: Label
    private var isBreakPoint = false
    private val errorBox: MUIPopupMessageBox
    private val warningBox: MUIPopupMessageBox
    private var defaultBackgroundFill: BackgroundFill

    constructor(line: Int): this("$line")
    constructor(text: String)
    {
        this.styleClass.add("mui-gutter-button")
        this.errorBox = MUIPopupMessageBox(FontAwesomeSolid.EXCLAMATION_CIRCLE, "", "")
        this.warningBox = MUIPopupMessageBox(FontAwesomeSolid.EXCLAMATION_TRIANGLE, "", "")
        this.errorBox.iconColor = Color.RED
        this.warningBox.iconColor = Color.rgb(237, 225, 7)
        this.errorBox.iconScale = 1.5
        this.warningBox.iconScale = 1.5
        this.iconView = FontIcon()
        this.label = Label(text)
        this.alignment = Pos.CENTER_LEFT
        this.spacing = 20.0
        this.prefWidth = 80.0
        this.prefHeight = 30.0
        this.children.addAll(this.iconView, this.label)
        this.padding = Insets(.0, 10.0, 0.0, 0.0)
        this.cursor = Cursor.DEFAULT
        this.label.font = fontProperty().get()
        this.fontProperty().addListener { _-> this.label.font = fontProperty().get() }
        if (this.background != null && this.background.fills != null && this.background.fills.size > 0)
            this.defaultBackgroundFill = this.background.fills.first()
        else
        {
            this.defaultBackgroundFill = BackgroundFill(Color.color(0.94118,  0.94118,  0.94118), CornerRadii.EMPTY, Insets.EMPTY)
            this.background = Background(this.defaultBackgroundFill, BackgroundFill(this.breakpointFill.get(), CornerRadii.EMPTY, Insets.EMPTY ))
        }
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, { event ->
            if (!this.iconView.boundsInParent.contains(event.x, event.y))
            {
                this.isBreakPoint = !this.isBreakPoint
                if (this.isBreakPoint)
                {
                    this.defaultBackgroundFill = this.background.fills.first()
                    this.background = Background(BackgroundFill(this.breakpointFill.get(), javafx.scene.layout.CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY))
                }
                else
                    this.background = Background( this.defaultBackgroundFill)
            }
        })

    }

    public fun showWarning(title: String, message: String)
    {
        this.iconView.iconCode = FontAwesomeSolid.EXCLAMATION_TRIANGLE
        this.iconView.fill = Color.rgb(237, 225, 7)
        this.iconView.addEventFilter(MouseEvent.MOUSE_CLICKED, {
            this.warningBox.title = title
            this.warningBox.message = message
            this.warningBox.show(this)
        })
    }
    public fun showError(title: String, message: String)
    {
        this.iconView.iconCode = FontAwesomeSolid.EXCLAMATION_CIRCLE
        this.iconView.fill = Color.RED
        this.iconView.addEventFilter(MouseEvent.MOUSE_CLICKED, {
            this.errorBox.title = title
            this.errorBox.message = message
            this.errorBox.show(this)
        })
    }

    /**************************************************************************
     *                                                                        *
     *                              Styling                                   *
     *                                                                        *
     **************************************************************************/

    public override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> = STYLEABLES


    /********************************
     *                              *
     *       Breakpoint fill        *
     *                              *
     ********************************/
    private val breakpointFill: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(BREAKPOINT_FILL, this, "breakpointFill")
    public fun breakpointFillProperty(): StyleableObjectProperty<Paint> = breakpointFill
    public fun getBreakPointFill(): Paint = breakpointFill.get()
    public fun setBreakPointFill(value: Paint) = breakpointFill.set(value)

    /********************************
     *                              *
     *             SerializableFont             *
     *                              *
     ********************************/

    private val font: StyleableObjectProperty<Font> = SimpleStyleableObjectProperty(FONT, this, "font")
    public fun fontProperty(): StyleableObjectProperty<Font> = font
    public fun getFont(): Font = font.get()
    public fun setFont(value: Font) = font.set(value)

    private companion object
    {
        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val BREAKPOINT_FILL = object: CssMetaData<MUIGutterButton, Paint>("-fx-breakpoint-fill", PaintConverter.getInstance(), Color.color(0.39608,  0.49412,  0.74118, 1.0)) {
            override fun isSettable(styleable: MUIGutterButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIGutterButton?): StyleableProperty<Paint> = styleable?.breakpointFillProperty()!!
        }

        internal val FONT = object: CssMetaData<MUIGutterButton, Font>("-fx-font", FontConverter.getInstance(), Font("Roboto Medium", 12.0)) {
            override fun isSettable(styleable: MUIGutterButton?): Boolean = true
            override fun getStyleableProperty(styleable: MUIGutterButton?): StyleableProperty<Font> = styleable?.fontProperty()!!
        }

        init
        {
            val styleables = MutableList<CssMetaData<out Styleable, *>>(Pane.getClassCssMetaData().size, { index -> Pane.getClassCssMetaData()[index]})
            Collections.addAll(styleables, BREAKPOINT_FILL, FONT)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
}


/*
    inner class Arrow: Pane
    {
        constructor()
        {
            this.prefWidth = 50.0
            this.prefHeight = 30.0
            val arrow = Polygon()
            //triangle.strokeWidth = 30.0
            arrow.fill = Color.BLACK//Color.color((101.0 / 255.0), (126.0 / 255.0), (189.0 / 255.0))
            //this.style = "-fx-background-color: rgb(101, 126, 189);"
            this.children.add(arrow)

            Platform.runLater {
                val points = ArrayList<Double>()

                arrow.points.addAll(points)
            }
        }
    }
                val points = ArrayList<Double>()
                points.add(0.0)
                points.add(0.0)
                points.add(0.0)
                points.add(this.height)
                points.add(15.0)
                points.add(this.height / 2.0)
                triangle.points.addAll(points)
                triangle.layoutX = (this.width - (this.height / 2.0))
* */