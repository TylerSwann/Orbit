package io.orbit.ui

import io.orbit.api.AutoCompletionOption
import io.orbit.api.LanguageDelegate
import io.orbit.text.TextDocumentEditor
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.effect.DropShadow
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.PopupWindow
import javafx.stage.Window
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 15:47
 */
class AutoCompletionDialog: PopupWindow, ParseTreeListener
{
    private val optionsContainer: VBox
    private val container: ScrollPane
    private val optionHeight = 30.0
    private val xOffset = 10.0
    private val yOffset = 0.0
    private val editor: TextDocumentEditor
    private var options: ArrayList<AutoCompletionNode>
    private var selectedIndex = 0
    private var selectedOption: AutoCompletionNode? = null
    private val language: LanguageDelegate
    private var scrollLength = 0.01
    private val walker: ParseTreeWalker
    private var currentWord = ""

    constructor(editor: TextDocumentEditor, language: LanguageDelegate)
    {
        this.language = language
        this.editor = editor
        this.container = ScrollPane()
        this.optionsContainer = VBox()
        this.options = ArrayList()
        this.walker = ParseTreeWalker()
        val width = 600.0
        val height = 250.0
        this.container.prefWidth = width
        this.container.prefHeight = height
        this.container.style = "-fx-background-color: white;\n-fx-background-radius: 5px;"
        val shadow = DropShadow(10.0, 1.5, 2.5, Color.GREY)
        this.container.effect = shadow

        this.optionsContainer.prefWidth = width
        this.optionsContainer.spacing = 10.0
        this.container.content = this.optionsContainer
        this.container.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        this.scene.root = this.container
        this.editor.ignoreKeys(KeyCode.UP, KeyCode.DOWN, KeyCode.ENTER)
        registerListeners()
    }

    private fun registerListeners()
    {
        this.editor.caretPositionProperty().addListener { _-> this.updatePosition(this.editor.caretBounds) }
        this.editor.addEventHandler(MouseEvent.MOUSE_CLICKED, { this.opacity = 0.0 })
        this.editor.plainTextChanges()
               .successionEnds(Duration.ofMillis(100))
               .addObserver { _ -> this.walker.walk(this, this.language.getPrimaryExpression(this.editor.text)) }

        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, { event ->
            this.opacity = 1.0
            val previousIndex = this.selectedIndex
            when (event.code)
            {
                KeyCode.BACK_SPACE -> this.opacity = 0.0
                KeyCode.ENTER -> {
                    if (this.selectedOption != null && this.opacity == 1.0)
                    {
                        val replacementText = this.selectedOption?.option?.insertedText ?: return@addEventHandler
                        val caretPos = this.editor.caretPosition
                        this.editor.replaceText(caretPos - currentWord.length, caretPos, replacementText)
                        this.updatePosition(this.editor.caretBounds)
                        this.currentWord = replacementText
                    }
                    else
                    {
                        val caretPos = this.editor.focusPosition.character
                        this.editor.insertText(caretPos, "\n")
                    }
                }
            }
            if (event.code.isArrowKey)
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
                val newOption = this.options[this.selectedIndex]
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

    private fun updateOptions(options: List<AutoCompletionOption>)
    {
        val optionsNodes = ArrayList<AutoCompletionNode>()
        options.forEach { optionsNodes.add(AutoCompletionNode(it)) }
        this.optionsContainer.children.removeAll(this.options)
        this.options = optionsNodes
        this.scrollLength = (1.0 / this.options.size.toDouble())
        this.optionsContainer.children.addAll(optionsNodes)
        this.optionsContainer.prefHeight = (options.size * (optionHeight + 10.0))
        if (!this.isShowing && this.options.isNotEmpty())
            this.show(this.editor.scene.window)
        if (this.options.isEmpty())
            this.opacity = 0.0
        else
        {
            this.opacity = 1.0
            this.selectedOption = this.options[0]
            this.options[0].isSelected = true
        }
    }

    public override fun show(owner: Window)
    {
        if (this.options.isNotEmpty())
        {
            this.options[0].isSelected = true
            this.selectedOption = this.options[0]
        }
        super.show(owner)
    }

    private fun updatePosition(optionalBounds: Optional<Bounds>)
    {
        if (optionalBounds.isPresent)
        {
            val bounds = optionalBounds.get()
            this.x = (bounds.minX + xOffset)
            this.y = (bounds.minY + yOffset)
        }
    }



    private inner class AutoCompletionNode: HBox
    {
        private val primaryLabel: Label
        val option: AutoCompletionOption
        var isSelected = false
            set(value)
            {
                if (value)
                    this.style = "-fx-background-color: rgba(0, 0, 0, 0.2);\n-fx-background-radius: 5px;"
                else
                    this.style = "-fx-background-color: transparent;\n-fx-background-radius: 5px;"
            }

        constructor(option: AutoCompletionOption)
        {
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, {
                options.forEach { it.isSelected = false }
                this.isSelected = true
                selectedOption = this
                selectedIndex = options.indexOf(this)
            })
            this.option = option
            this.prefHeight = optionHeight
            this.alignment = Pos.CENTER_LEFT
            this.spacing = 5.0
            this.style = "-fx-background-radius: 5px;"
            this.primaryLabel = Label(option.option)
            primaryLabel.padding = Insets(0.0, 0.0, 0.0, 25.0)
            primaryLabel.font = Font("Roboto Medium", 16.0)
            this.children.add(primaryLabel)
            option.details
                    .map { Label(it) }
                    .forEach { this.children.add(it) }
        }
    }


    override fun enterEveryRule(ctx: ParserRuleContext?) { }

    override fun exitEveryRule(ctx: ParserRuleContext?) { }

    override fun visitErrorNode(node: ErrorNode?)
    {
        val errNode = node ?: return
        if (errNode.text == "}" ||
                errNode.text.matches("<.*>".toRegex()) ||
                this.language.autoCompletionProvider == null)
            return
        this.currentWord = errNode.text
        val options = this.language.autoCompletionProvider.optionsFor(
                                                                errNode.symbol.startIndex,
                                                                errNode.symbol.stopIndex,
                                                                errNode.symbol.line,
                                                                errNode.text)
        if (options == null || options.isEmpty())
            this.opacity = 0.0
        this.updateOptions(options)
    }

    override fun visitTerminal(node: TerminalNode?) { }
}