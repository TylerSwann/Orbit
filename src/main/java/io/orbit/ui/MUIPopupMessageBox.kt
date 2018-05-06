package io.orbit.ui

import com.sun.javafx.css.converters.FontConverter
import com.sun.javafx.css.converters.PaintConverter
import io.orbit.util.frameOfNode
import javafx.animation.ScaleTransition
import javafx.application.Platform
import javafx.css.*
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.PopupControl
import javafx.scene.effect.DropShadow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.stage.WindowEvent
import javafx.util.Duration
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.javafx.FontIcon
import java.util.*


/**
 * Created by Tyler Swann on Sunday February 11, 2018 at 14:12
 */
class MUIPopupMessageBox: PopupControl
{
    private val root: AnchorPane
    private val container: VBox
    private val headerContainer: Pane
    private val messageLabel: Label
    private val headerLabel: Label
    private val headerIcon: FontIcon
    private var parentScene: Scene? = null
    public var iconColor = Color.BLACK
        set(value) {  this.headerIcon.fill = value  }
    public var message = "Message Goes Here..."
        set(value) {  this.messageLabel.text = value  }
    public var title = "MUIPopupMessageBox"
        set(value) {  this.headerLabel.text = value  }
    public var iconScale = 1.0
        set(value) {
            this.headerIcon.scaleX = value
            this.headerIcon.scaleY = value
        }

    constructor(icon: Ikon, title: String, message: String)
    {
        // TODO - styleable properties
        this.root = AnchorPane()
        this.container = VBox()
        this.headerContainer = Pane()
        this.messageLabel = Label()
        this.headerLabel = Label()
        this.message = message
        this.title = title
        this.headerIcon = FontIcon(icon)
        this.setPrefSize(300.0, 200.0)
        this.headerContainer.setPrefSize(300.0, 30.0)
        this.headerContainer.children.addAll(this.headerIcon, this.headerLabel)
        this.messageLabel.textAlignment = TextAlignment.CENTER
        this.messageLabel.isWrapText = true
        this.messageLabel.padding = Insets(0.0, 10.0, 0.0, 10.0)
        this.messageLabel.textAlignment = TextAlignment.CENTER
        this.messageLabel.padding = Insets(30.0, 5.0, 0.0, 5.0)
        this.headerLabel.textAlignment = TextAlignment.CENTER
        this.headerLabel.alignment = Pos.CENTER
        this.container.setPrefSize(300.0, 150.0)
        this.container.alignment = Pos.TOP_CENTER
        applyAnchors(this.container, 0.0, 0.0, 0.0, 0.0)
        this.container.children.addAll(this.headerContainer, this.messageLabel)
        this.root.children.add(this.container)
        this.scene.root = this.root
        val shadow = DropShadow()
        shadow.spread = 0.0
        shadow.offsetY = 2.0
        shadow.offsetX = 1.5
        shadow.color = Color.GREY
        this.root.effect = shadow
        //this.root.styleClass.add("mui-popup-message-box")
        this.addStyleListeners()
        //TODO - remove this
       // this.root.style = "-fx-background-radius: 5;"
        this.root.style = "-fx-background-radius: 5;\n-fx-background-color: white;"
        this.headerContainer.style = "-fx-background-radius: 5 5 0 0;\n-fx-background-color: rgba(150, 150, 150, 0.1);"
        this.styleClass.add("mui-popup-message-box")
        this.headerLabel.font = Font("Roboto Black", 14.0)
        this.messageLabel.font = Font("Roboto Light", 14.0)
    }



    public fun show(owner: Node)
    {
        val frame = frameOfNode(owner)
        val x = frame.x + (frame.width / 2.0)
        val y = frame.y + (frame.height / 2.0)
        if (this.parentScene == null)
        {
            this.parentScene = owner.scene
            this.parentScene?.addEventFilter(MouseEvent.MOUSE_PRESSED, {
                if (this.isShowing)
                    this.hide()
            })
            this.parentScene?.window?.addEventFilter(WindowEvent.WINDOW_HIDING, {_-> this.hide() })
        }
        this.root.scaleY = 0.0
        this.root.scaleX = 0.0
        super.show(owner, x, y)
        val transition = ScaleTransition(Duration(100.0), this.root)
        transition.fromY = 0.0
        transition.fromX = 0.0
        transition.toX = 1.0
        transition.toY = 1.0
        transition.play()
        Platform.runLater {
            this.headerLabel.layoutX = ((this.headerContainer.width / 2.0) - (this.headerLabel.width / 2.0))
            this.headerLabel.layoutY = ((this.headerContainer.height / 2.0) - (this.headerLabel.height / 2.0))
            this.headerIcon.layoutX = this.headerIcon.boundsInLocal.width
            this.headerIcon.layoutY = ((this.headerIcon.boundsInLocal.height / 2.0) + (this.headerContainer.height / 2.0))
        }
    }

    private fun addStyleListeners()
    {
        val applyStyles: (() -> Unit) = {
            this.headerLabel.font = this.getHeaderFont()
            this.messageLabel.font = this.getMessageFont()
            this.messageLabel.textFill = this.getMessageFontColor()
            this.headerLabel.textFill = this.getHeaderFontColor()
            //println(this.getMessageFontColor().equals(Color.BLUE))
            //println("Black?: ${this.getMessageFontColor().equals(Color.BLACK)}")
        }
        applyStyles()
        this.headerFontProperty().addListener { _-> applyStyles() }
        this.messageFontProperty().addListener { _-> applyStyles() }
        this.headerFontColorProperty().addListener { _-> applyStyles() }
        this.messageFontColorProperty().addListener { _-> applyStyles() }
    }

    public override fun hide()
    {
        val transition = ScaleTransition(Duration(100.0), this.root)
        transition.fromY = 1.0
        transition.fromX = 1.0
        transition.toX = 0.0
        transition.toY = 0.0
        transition.setOnFinished { super.hide() }

        transition.play()
    }

    private fun applyAnchors(node: Node, top: Double?, bottom: Double?, left: Double?, right: Double?)
    {
        AnchorPane.setTopAnchor(node, top)
        AnchorPane.setBottomAnchor(node, bottom)
        AnchorPane.setLeftAnchor(node, left)
        AnchorPane.setRightAnchor(node, right)

    }

    /**************************************************************************
     *                                                                        *
     *                              Styling                                   *
     *                                                                        *
     **************************************************************************/

    /*
    * Header/message font
    * Header/message font color
    * Header background color
    * Body background color
    * Body background radius
    * */

    // TODO - fix this


    public override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> = STYLEABLES


    /**
     * Header SerializableFont
     */
    private val headerFont: StyleableObjectProperty<Font> = SimpleStyleableObjectProperty(HEADER_FONT, this, "headerFont", Font("Roboto Black", 12.0))
    public fun headerFontProperty(): StyleableObjectProperty<Font> = headerFont
    public fun getHeaderFont(): Font = headerFont.get()
    public fun setHeaderFont(value: Font) = headerFont.set(value)

    /**
     * Message SerializableFont
     * */
    private val messageFont: StyleableObjectProperty<Font> = SimpleStyleableObjectProperty(MESSAGE_FONT, this, "messageFont", Font("Roboto Medium", 12.0))
    public fun messageFontProperty(): StyleableObjectProperty<Font> = messageFont
    public fun getMessageFont(): Font = messageFont.get()
    public fun setMessageFont(value: Font) = messageFont.set(value)

    /**
     * Header SerializableFont Color
     * */
    private val headerFontColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(HEADER_FONT_COLOR, this, "headerFontColor", Color.BLACK)
    public fun headerFontColorProperty(): StyleableObjectProperty<Paint> = headerFontColor
    public fun getHeaderFontColor(): Paint = headerFontColor.get()
    public fun setHeaderFontColor(value: Paint) = headerFontColor.set(value)

    /**
     * Message SerializableFont Color
     * */
    private val messageFontColor: StyleableObjectProperty<Paint> = SimpleStyleableObjectProperty(MESSAGE_FONT_COLOR, this, "messageFontColor", Color.BLACK)
    public fun messageFontColorProperty(): StyleableObjectProperty<Paint> = messageFontColor
    public fun getMessageFontColor(): Paint = messageFontColor.get()
    public fun setMessageFontColor(value: Paint) = messageFontColor.set(value)

    private companion object
    {

        internal val STYLEABLES: MutableList<CssMetaData<out Styleable, *>>

        internal val HEADER_FONT = object: CssMetaData<MUIPopupMessageBox, Font>("-fx-header-font", FontConverter.getInstance(), Font("Roboto Black", 15.0)) {
            override fun isSettable(styleable: MUIPopupMessageBox?): Boolean = true
            override fun getStyleableProperty(styleable: MUIPopupMessageBox?): StyleableProperty<Font> = styleable?.headerFontProperty()!!
        }

        internal val MESSAGE_FONT = object: CssMetaData<MUIPopupMessageBox, Font>("-fx-message-font", FontConverter.getInstance(), Font("Roboto Medium", 13.0)) {
            override fun isSettable(styleable: MUIPopupMessageBox?): Boolean = true
            override fun getStyleableProperty(styleable: MUIPopupMessageBox?): StyleableProperty<Font> = styleable?.messageFontProperty()!!
        }
        internal val HEADER_FONT_COLOR = object: CssMetaData<MUIPopupMessageBox, Paint>("-header-font-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUIPopupMessageBox?): Boolean = true
            override fun getStyleableProperty(styleable: MUIPopupMessageBox?): StyleableProperty<Paint> = styleable?.headerFontColorProperty()!!
        }
        internal val MESSAGE_FONT_COLOR = object: CssMetaData<MUIPopupMessageBox, Paint>("-message-font-color", PaintConverter.getInstance(), Color.BLACK) {
            override fun isSettable(styleable: MUIPopupMessageBox?): Boolean = true
            override fun getStyleableProperty(styleable: MUIPopupMessageBox?): StyleableProperty<Paint> = styleable?.messageFontColorProperty()!!
        }
        //public fun getClassCssMetaData(): List<CssMetaData<out Styleable, *>> = STYLEABLES
        init
        {

            val styleables = MutableList<CssMetaData<out Styleable, *>>(PopupControl.getClassCssMetaData().size, { index -> PopupControl.getClassCssMetaData()[index]})
            Collections.addAll(styleables, HEADER_FONT, MESSAGE_FONT, HEADER_FONT_COLOR, MESSAGE_FONT_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }
}