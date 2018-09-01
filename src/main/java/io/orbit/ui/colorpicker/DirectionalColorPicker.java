package io.orbit.ui.colorpicker;

import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.LinearGradient;

/**
 * Created by Tyler Swann on Sunday August 26, 2018 at 15:44
 */
public class DirectionalColorPicker extends ChooserPane
{
    private static final PseudoClass VERTICAL = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL = PseudoClass.getPseudoClass("horizontal");
    private static final String multiColorGradient = "linear-gradient(from 100.0% 0.0% to 100.0% 100.0%, red 0.0%, yellow 40.2%, white 70.6%, blue 100.0%)";
    private Orientation orientation = Orientation.VERTICAL;
    private String gradient;
    enum Orientation { VERTICAL, HORIZONTAL }

    public DirectionalColorPicker(Orientation orientation)
    {
        super(new Pane(), false, true);
        setOrientation(orientation);
        setGradient(multiColorGradient);
        this.getKnob().getStyleClass().add("directional-knob");
    }

    public void setGradient(String gradient)
    {
        this.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf(gradient), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
        if (this.getKnob().prefWidthProperty().isBound())
            this.getKnob().prefWidthProperty().unbind();
        if (this.prefHeightProperty().isBound())
            this.getKnob().prefHeightProperty().unbind();
        if (orientation == Orientation.VERTICAL)
        {
            this.getKnob().pseudoClassStateChanged(VERTICAL, true);
            this.getKnob().pseudoClassStateChanged(HORIZONTAL, false);
            this.getKnob().prefWidthProperty().bind(this.widthProperty());
            this.allowTranslateX(false);
            this.allowTranslateY(true);
            this.setPrefWidth(30.0);
        }
        else
        {
            this.getKnob().pseudoClassStateChanged(VERTICAL, false);
            this.getKnob().pseudoClassStateChanged(HORIZONTAL, true);
            this.getKnob().prefHeightProperty().bind(this.heightProperty());
            this.setPrefHeight(30.0);
            this.allowTranslateX(true);
            this.allowTranslateY(false);
        }
    }

    public Orientation getOrientation() { return orientation; }
    public String getGradient() { return gradient; }
}
