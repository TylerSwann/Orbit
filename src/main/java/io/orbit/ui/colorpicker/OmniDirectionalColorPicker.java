package io.orbit.ui.colorpicker;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.util.Duration;


/**
 * Created by Tyler Swann on Sunday August 26, 2018 at 15:27
 */
public class OmniDirectionalColorPicker extends ChooserPane
{
    private Color base;

    public OmniDirectionalColorPicker(Color base)
    {
        super(new Pane(), true, true);
        this.getKnob().getStyleClass().add("omnidirectional-knob");
        this.build();
        setBase(base);
    }

    private void build()
    {
        this.getKnob().addEventHandler(MouseEvent.MOUSE_PRESSED, __ -> scaleKnob(true));
        this.getKnob().addEventFilter(MouseEvent.MOUSE_RELEASED, __ -> scaleKnob(false));
    }

    private void scaleKnob(boolean in)
    {

        double to = in ? 1.5 : 1.0;
        double from = in ? 1.0 : 1.5;
        ScaleTransition scale = new ScaleTransition(Duration.millis(80.0), this.getKnob());
        scale.setFromY(from);
        scale.setFromX(from);
        scale.setToY(to);
        scale.setToX(to);
        scale.play();
    }

    public Color getBase() { return base; }
    public void setBase(Color base)
    {
        this.base = base;
        BackgroundFill primary = new BackgroundFill(base, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill secondary = new BackgroundFill(LinearGradient.valueOf("linear-gradient(to right, white 0%, rgba(255, 255, 255, 0) 100%)"), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill tertiary = new BackgroundFill(LinearGradient.valueOf("linear-gradient(to bottom, transparent 0%, black 100%)"), CornerRadii.EMPTY, Insets.EMPTY);
        this.setBackground(new Background(primary, secondary, tertiary));
    }
}
