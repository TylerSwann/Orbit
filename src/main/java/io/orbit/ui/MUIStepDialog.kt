package io.orbit.ui

import com.jfoenix.controls.JFXButton
import javafx.animation.TranslateTransition
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.util.Duration

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 09:35
 */
class MUIStepDialog: VBox
{
    private val controlsContainer = HBox()
    private val stepContainer = Pane()
    private val next = JFXButton("NEXT")
    private val back = JFXButton("BACK")
    private var steps: Array<Pane> = emptyArray()
    private var needsSetup = true
    private var isTransitioning = false
    private var isOnFinalStep = false
    private val controlsContainerHeight = 50.0
    private var index = 0


    constructor()
    {
        this.styleClass.add("mui-step-dialog")
        this.next.styleClass.add("button-primary")
        this.back.styleClass.add("button-secondary")
    }

    public fun setSteps(steps: Array<Pane>)
    {
        if (needsSetup)
            this.setup(steps)
        else
        {
            this.stepContainer.children.removeAll(this.steps)
            this.steps = steps
            this.stepContainer.children.addAll(this.steps)
            this.index = 0
            Platform.runLater { this.recalculateAll() }
        }
    }

    private fun setup(steps: Array<Pane>)
    {
        this.steps = steps
        this.widthProperty().addListener({_-> this.recalculateAll() })
        this.heightProperty().addListener({_-> this.recalculateAll() })
        val buttons = arrayOf(this.next, this.back)
        buttons.forEach {
            it.prefWidth = 88.0
            it.prefHeight = 36.0
        }
        this.controlsContainer.alignment = Pos.CENTER_RIGHT
        this.controlsContainer.spacing = 15.0
        this.controlsContainer.padding = Insets(0.0, 10.0, 0.0, 0.0)
        this.alignment = Pos.CENTER
        this.controlsContainer.children.addAll(this.back, this.next)
        this.stepContainer.children.addAll(this.steps)
        this.children.addAll(this.stepContainer, this.controlsContainer)
        this.back.isDisable = true
        if (steps.size == 1)
        {
            this.next.text = "CREATE"
            this.isOnFinalStep = true
        }
        this.next.isDisable = steps.isEmpty()
        this.next.setOnAction {
            if (!this.isOnFinalStep)
                this.forward(500.0)
            else
                this.fireEvent(MUIStepDialogEvent(this, this, MUIStepDialogEvent.COMPLETION))
        }
        this.back.setOnAction { this.backward(500.0) }
        Platform.runLater { this.recalculateAll() }
        this.needsSetup = false
    }
    private fun recalculateAll()
    {
        this.recalculateSizing()
        Platform.runLater { this.recalculatePositioning() }
    }

    private fun recalculateSizing()
    {
        this.controlsContainer.prefHeight = controlsContainerHeight
        this.controlsContainer.prefWidth = this.width
        this.stepContainer.prefWidth = this.width
        this.stepContainer.prefHeight = this.height - controlsContainerHeight
        this.steps.forEach {
            it.prefWidth = this.width
            it.prefHeight = this.height - controlsContainerHeight
        }
    }
    private fun recalculatePositioning()
    {
        var offsetX = 0.0
        for (step in this.steps)
        {
            step.translateX = offsetX
            step.translateY = 0.0
            offsetX += this.width
        }
    }
    private fun forward(duration: Double)
    {
        if (this.isTransitioning) return
        this.fireEvent(MUIStepDialogEvent(this, this, MUIStepDialogEvent.NEXT_STEP))
        this.isTransitioning = true
        if (this.index + 1 == this.steps.size - 1)
        {
            this.next.text = "CREATE"
            this.isOnFinalStep = true
        }
        this.index++
        this.steps.forEach { step ->
            this.back.isDisable = false
            val transition = TranslateTransition(Duration(duration), step)
            transition.toX = (step.translateX - this.width)
            transition.fromX = step.translateX
            transition.setOnFinished {
                if (step == this.steps.last())
                    this.isTransitioning = false
            }
            transition.play()
        }
    }
    private fun backward(duration: Double)
    {
        if (this.isTransitioning) return
        this.fireEvent(MUIStepDialogEvent(this, this, MUIStepDialogEvent.PREVIOUS_STEP))
        this.isTransitioning = true
        if (this.index - 1 <= 0)
            this.back.isDisable = true
        if (this.index == this.steps.size - 1)
        {
            this.isOnFinalStep = false
            this.next.text = "NEXT"
        }
        this.index--
        steps.forEach { step ->
            this.next.isDisable = false
            val transition = TranslateTransition(Duration(duration), step)
            transition.toX = (step.translateX + this.width)
            transition.fromX = step.translateX
            transition.setOnFinished {
                if (step == steps.last())
                    this.isTransitioning = false
            }
            transition.play()
        }
    }
}