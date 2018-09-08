package io.orbit.ui.colorpicker;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Tyler Swann on Friday August 31, 2018 at 17:13
 */
public class TextFieldSlider extends VBox
{
    private static final String DEFAULT_STYLE_CLASS = "controls-container";

    private Label label;
    private TextField textField;
    private JFXSlider slider;

    private final double min;
    private final double max;
    private final boolean useIntegerValues;

    public double getValue() { return this.slider.getValue(); }
    public DoubleProperty valueProperty() { return this.slider.valueProperty(); }
    public void setValue(double value) { this.slider.setValue(value); }

    public TextFieldSlider(String text, double min, double max, double value, boolean useIntegerValues)
    {
        this.min = min;
        this.max = max;
        this.useIntegerValues = useIntegerValues;
        this.slider = new JFXSlider(min, max, value);
        this.setValue(value);
        this.label = new Label(text);
        if (useIntegerValues)
            this.textField = new TextField(String.valueOf(Math.round(value)));
        else
            this.textField = new TextField(String.valueOf(value));
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        build();
    }

    private void build()
    {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0.0000, 0.0, 20.0, 0.0));

        HBox controlsContainer = new HBox();
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.setSpacing(10.0);
        controlsContainer.getChildren().addAll(this.slider, this.textField);
        this.getChildren().addAll(this.label, controlsContainer);
        registerListeners();
    }

    private void registerListeners()
    {
        this.slider.valueProperty().addListener(__ -> {
            if (useIntegerValues)
                this.textField.setText(String.valueOf(Math.round(this.slider.getValue())));
            else
                this.textField.setText(String.valueOf(this.slider.getValue()));
        });
        this.textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9]+(\\.[0-9]+)?"))
            {
                this.textField.setText(oldVal);
                return;
            }
            try
            {
                double value = Double.parseDouble(newVal);
                if (value < this.min || value > this.max)
                    this.textField.setText(String.valueOf(oldVal));
                else
                {
                    if (useIntegerValues)
                        this.textField.setText(String.valueOf(Math.round(value)));
                    else
                        this.textField.setText(String.valueOf(value));
                    this.setValue(value);
                }
            }
            catch (NumberFormatException ex) { this.textField.setText(oldVal); }
        });
    }
}
