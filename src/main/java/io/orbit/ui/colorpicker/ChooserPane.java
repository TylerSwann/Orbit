package io.orbit.ui.colorpicker;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Created by Tyler Swann on Sunday August 26, 2018 at 19:06
 */
public class ChooserPane extends Pane
{
    private Pane knob;
    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private boolean allowTranslateX;
    private boolean allowTranslateY;

    public ChooserPane(boolean allowTranslateX, boolean allowTranslateY)
    {
        this.knob = new Pane();
        this.allowTranslateX = allowTranslateX;
        this.allowTranslateY = allowTranslateY;
        registerListeners();
    }

    private void registerListeners()
    {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            this.mouseX = event.getSceneX();
            this.mouseY = event.getSceneY();
            double x = event.getX() - (this.knob.getWidth() / 2.0);
            double y = event.getY() - (this.knob.getHeight() / 2.0);
            if (this.allowTranslateX && this.allowTranslateY)
                if (!isXOutOfBounds(x) && !isYOutOfBounds(y))
                    this.knob.relocate(x, y);
            else if (this.allowTranslateX)
                if (!isXOutOfBounds(x))
                    this.knob.relocate(x, knob.getLayoutY());
            else if (this.allowTranslateY)
                if (!isYOutOfBounds(y))
                    this.knob.relocate(knob.getLayoutX(), y);
        });
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);
        this.getChildren().add(knob);
    }

    private void drag(MouseEvent event)
    {
        double deltaX = event.getSceneX() - this.mouseX;
        double deltaY = event.getSceneY() - this.mouseY;
        deltaX += knob.getLayoutX();
        deltaY += knob.getLayoutY();
        double x = isXOutOfBounds(deltaX) || !allowTranslateX ? knob.getLayoutX() : deltaX;
        double y = isYOutOfBounds(deltaY) || !allowTranslateY ? knob.getLayoutY() : deltaY;
        this.knob.relocate(x, y);
        if (!isXOutOfBounds(deltaX))
            this.mouseX = event.getSceneX();
        if (!isYOutOfBounds(deltaY))
            this.mouseY = event.getSceneY();
    }

    private boolean isXOutOfBounds(double x)
    {
        return (x > (this.getTranslateX() + this.getWidth()) - knob.getWidth()) || (x < this.getTranslateX());
    }

    private boolean isYOutOfBounds(double y)
    {
        return (y > (this.getTranslateY() + this.getHeight()) - knob.getHeight()) || (y < this.getTranslateY());
    }

    public Pane getKnob() { return knob; }

    public void allowTranslateX(boolean allow) { this.allowTranslateX = allow; }
    public void allowTranslateY(boolean allow) { this.allowTranslateY = allow; }
}
