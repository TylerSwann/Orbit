package io.orbit.api.autocompletion

import io.orbit.api.event.AutoCompletionEvent
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.PopupControl
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import kotlin.collections.ArrayList

/**
 * Created by Tyler Swann on Wednesday March 21, 2018 at 13:03
 */
class AutoCompletionDialog: PopupControl
{
    private val optionsContainer: VBox
    private val container: ScrollPane
    private val optionHeight = 30.0
    private val xOffset = 10.0
    private val yOffset = 0.0
    private var selectedIndex = 0
    private var selectedOptionProperty = SimpleObjectProperty<AutoCompletionNode>()
    private var selectedOption: AutoCompletionNode?
        set(value) = selectedOptionProperty.set(value)
        get() = selectedOptionProperty.get()
    private var scrollLength = 0.01
    private val owner: Node
    private var optionNodes: ArrayList<AutoCompletionNode>
    public var options: List<AutoCompletionBase> = emptyList()
    public var visibilityProperty = SimpleObjectProperty<Boolean>(false)
    public var isVisible: Boolean
        set(value) = visibilityProperty.set(value)
        get() = visibilityProperty.get()
    private var isShowingSubOptions = false

    constructor(owner: Node)
    {
        this.visibilityProperty.addListener { _ ->
            this.opacity = if (this.isVisible) 1.0 else 0.0
        }
        this.owner = owner
        this.container = ScrollPane()
        this.optionsContainer = VBox()
        this.optionNodes = ArrayList()
        val width = 600.0
        val height = 250.0
        this.container.prefWidth = width
        this.container.prefHeight = height
        this.optionsContainer.prefWidth = width
        this.optionsContainer.spacing = 10.0
        this.optionsContainer.minHeight = height
        this.container.content = this.optionsContainer
        this.container.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        this.scene.root = this.container
        this.container.backgroundProperty()
        registerListeners()
        this.container.styleClass.add("auto-completion-dialog")
        this.optionsContainer.styleClass.add("auto-completion-dialog")
    }


    public fun updateOptions(options: List<AutoCompletionBase>)
    {
        val optionsNodes = ArrayList<AutoCompletionNode>()
        options.forEach { optionsNodes.add(AutoCompletionNode(it, this.optionHeight)) }
        this.options = options
        this.optionsContainer.children.removeAll(this.optionNodes)
        this.optionNodes = optionsNodes
        this.scrollLength = (1.0 / this.optionNodes.size.toDouble())
        this.optionsContainer.children.addAll(optionsNodes)
        this.isVisible = true
        this.selectedIndex = 0
        this.selectedOption = this.optionNodes[0]
        this.optionNodes[0].isSelected = true
    }

    public fun show(x: Double, y: Double)
    {
        if (!this.isShowing)
            super.show(this.owner.scene.window)
        this.updatePosition(x, y)
        this.isVisible = true
    }

    public override fun hide()
    {
        this.isVisible = false
    }

    public fun updatePosition(x: Double, y: Double)
    {
        this.x = x + xOffset
        this.y = y + yOffset
    }

    private fun registerListeners()
    {
        this.owner.addEventHandler(KeyEvent.KEY_PRESSED, { event ->
            val previousIndex = this.selectedIndex
            if (event.code == KeyCode.ENTER && this.isVisible)
            {
                val selectedOption = this.selectedOption ?: return@addEventHandler
                if (!isShowingSubOptions)
                {
                    this.fireEvent(AutoCompletionEvent(selectedOption.option, this, this, AutoCompletionEvent.OPTION_WAS_SELECTED))
                    if (selectedOption.option.subOptions != null && selectedOption.option.subOptions.isNotEmpty())
                    {
                        this.updateOptions(selectedOption.option.subOptions)
                        this.isShowingSubOptions = true
                    }
                }
                else
                {
                    this.fireEvent(AutoCompletionEvent(selectedOption.option, this, this, AutoCompletionEvent.SUB_OPTION_WAS_SELECTED))
                    this.isShowingSubOptions = false
                }
            }
            else if(event.code == KeyCode.ENTER && !this.isVisible)
                this.isShowingSubOptions = false
            if (event.code.isArrowKey && this.isVisible)
            {
                when (event.code)
                {
                    KeyCode.UP -> {
                        if (this.selectedIndex == 0)
                            this.selectedIndex = this.options.size - 1
                        else
                            this.selectedIndex--
                    }
                    KeyCode.DOWN -> {
                        if (this.selectedIndex == this.options.size - 1)
                            this.selectedIndex = 0
                        else
                            this.selectedIndex++
                    }
                    else -> return@addEventHandler
                }
                this.selectedOption?.isSelected = false
                val newOption = this.optionNodes[this.selectedIndex]
                newOption.isSelected = true
                this.selectedOption = newOption
                val vValue = (this.container.vvalue + 0.01)
                when {
                    this.selectedIndex == 0 -> this.container.vvalue = 0.0
                    this.selectedIndex == this.options.size - 1 -> this.container.vvalue = 1.0
                    else -> this.container.vvalue = if (this.selectedIndex > previousIndex) vValue + scrollLength else vValue - scrollLength
                }
            }
        })
    }
    private class AutoCompletionNode: HBox
    {
        private val primaryLabel: Label
        val option: AutoCompletionBase
        val selectedProperty = SimpleBooleanProperty(false)
        var isSelected: Boolean
            get() = selectedProperty.get()
            set(value) = selectedProperty.set(value)

        constructor(option: AutoCompletionBase, optionHeight: Double)
        {
            this.option = option
            this.prefHeight = optionHeight
            this.alignment = Pos.CENTER_LEFT
            this.spacing = 5.0
            this.primaryLabel = Label(option.option)
            primaryLabel.padding = Insets(0.0, 0.0, 0.0, 25.0)
            this.children.add(primaryLabel)
            this.selectedProperty.addListener({_ -> this.pseudoClassStateChanged(SELECTED, this.isSelected) })
            this.styleClass.add("auto-completion-node")
        }

        companion object
        {
            private val SELECTED = PseudoClass.getPseudoClass("selected")
        }
    }
}





