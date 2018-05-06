package io.orbit.ui

import com.jfoenix.controls.JFXButton
import javafx.animation.TranslateTransition
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.util.Duration
import javax.swing.Timer


/**
 * Created by Tyler Swann on Friday January 12, 2018 at 16:08
 */
class MUIStepDialog: VBox
{
    private val stepControlPane = HBox()
    private val dialogPane = Pane()
    private val next = MUIMenuButton("NEXT")
    private val back = MUIMenuButton("BACK")
    private var offsetWidth = 0.0
    private var currentIndex = 0
    private var isTransitioning = false
    private var steps: Array<Pane> = emptyArray()
    private var needsSetup = true

    constructor()

    constructor(steps: Array<Pane>)
    {
        this.setSteps(steps)
    }

    public fun setSteps(steps: Array<Pane>)
    {
        if (this.needsSetup)
            this.setup(steps)
        else
        {
            this.dialogPane.children.removeAll(this.steps)
            this.steps = steps
            this.dialogPane.children.addAll(this.steps)
            this.back.isDisable = true
            this.next.isDisable = this.steps.size <= 1
        }
    }

    private fun setup(steps: Array<Pane>)
    {
        this.steps = steps
        val stepPaneHeight = 50.0
        this.stepControlPane.alignment = Pos.CENTER_RIGHT
        this.stepControlPane.spacing = 20.0
        this.stepControlPane.padding = Insets(0.0, 20.0, 0.0, 0.0)
        this.stepControlPane.prefHeight = stepPaneHeight
        this.next.prefWidth = 88.0
        this.back.prefWidth = 88.0
        this.next.prefHeight = 36.0
        this.back.prefHeight = 36.0
        this.widthProperty().addListener({_ ->
            this.stepControlPane.prefWidth = this.width
            this.dialogPane.prefWidth = this.width
            this.offsetWidth = this.width
            this.steps.forEach { step -> step.prefWidth = this.width }
            val timer = Timer(5, { recalculatePositioning() })
            timer.isRepeats = false
            timer.start()
        })
        this.heightProperty().addListener({_ ->
            this.dialogPane.prefHeight = (this.height - stepPaneHeight)
        })
        this.dialogPane.heightProperty().addListener({_ -> this.steps.forEach { it.prefHeight = this.dialogPane.height } })
        this.stepControlPane.children.addAll(this.back, this.next)
        this.children.addAll(this.dialogPane, this.stepControlPane)
        if (this.steps.size < 0) return
        this.back.isDisable = true
        this.next.isDisable = this.steps.size <= 1
        Platform.runLater {
            this.dialogPane.children.addAll(this.steps)
            this.steps.forEach { step -> step.prefWidth = this.width }
            this.dialogPane.prefWidth = this.width
            this.dialogPane.prefHeight = (this.height - stepPaneHeight)
            recalculatePositioning()
        }
        this.next.setOnAction { this.forward(500.0) }
        this.back.setOnAction { this.back(500.0) }
        //this.next.styleClass.add("primary-button")
        //this.back.styleClass.add("secondary-button")
        this.sceneProperty().addListener({_ ->
            if (this.scene != null)
                this.scene.widthProperty().addListener({_ -> recalculatePositioning() })
        })
        this.needsSetup = false
    }

    private fun recalculatePositioning()
    {
        var offset = 0.0
        this.steps.forEach { step ->
            step.translateX = 0.0
            step.translateX = offset
            offset += step.width
        }
        // TODO - fix issue where incorrect index is displayed on window is maximized
    }

    private fun forward(duration: Double)
    {
        if (this.isTransitioning) return
        this.isTransitioning = true
        if (this.currentIndex + 1 == this.steps.size - 1)
            this.next.isDisable = true
        this.currentIndex++
        this.steps.forEach { step ->
            this.back.isDisable = false
            val transition = TranslateTransition(Duration(duration), step)
            transition.toX = (step.translateX - this.offsetWidth)
            transition.fromX = step.translateX
            transition.setOnFinished {
                if (step == this.steps.last())
                    this.isTransitioning = false
            }
            transition.play()
        }
    }
    private fun back(duration: Double)
    {
        if (this.isTransitioning) return
        this.isTransitioning = true
        if (this.currentIndex - 1 <= 0)
            this.back.isDisable = true
        this.currentIndex--
        steps.forEach { step ->
            this.next.isDisable = false
            val transition = TranslateTransition(Duration(duration), step)
            transition.toX = (step.translateX + this.offsetWidth)
            transition.fromX = step.translateX
            transition.setOnFinished {
                if (step == steps.last())
                    this.isTransitioning = false
            }
            transition.play()
        }
    }
}