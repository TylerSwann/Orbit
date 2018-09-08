package io.orbit.ui.colorpicker;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Created by Tyler Swann on Monday September 03, 2018 at 14:26
 */
public class HSLColorPicker extends HBox
{
    private static final String DEFAULT_STYLE_CLASS = "hsl-color-picker";
    private static final double gradientSize = 250.0;

    private Slider huePicker;
    private Slider transparencyPicker;
    private SLColorChooser slChooser;
    private AnchorPane subControlsContainer;

    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>();
    public Color getColor() { return color.get(); }
    public ObservableValue<Color> colorProperty() { return color; }

    public double getHue() { return huePicker.getValue(); }
    public ReadOnlyDoubleProperty hueProperty() { return huePicker.valueProperty(); }

    public double getSaturation() { return slChooser.saturationProperty().get(); }
    public ReadOnlyDoubleProperty saturationProperty() { return slChooser.saturationProperty(); }

    public double getLightness() { return slChooser.lightnessProperty().get(); }
    public ReadOnlyDoubleProperty lightnessProperty() { return slChooser.lightnessProperty(); }

    public double getTransparency() { return transparencyPicker.getValue(); }
    public ReadOnlyDoubleProperty transparencyProperty() { return transparencyPicker.valueProperty(); }

    public HSLColorPicker(Color initialColor)
    {
        this.color.set(initialColor);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.huePicker = new Slider(0.0, 360.0, initialColor.getHue());
        this.transparencyPicker = new Slider(0.0, 1.0, initialColor.getOpacity());
        this.slChooser = new SLColorChooser(initialColor);
        this.subControlsContainer = new AnchorPane();
        build();
    }

    private void build()
    {
        this.setAlignment(Pos.CENTER);
        this.huePicker.setOrientation(Orientation.VERTICAL);
        this.transparencyPicker.setOrientation(Orientation.VERTICAL);

        this.transparencyPicker.getStyleClass().add("opacity");
        this.huePicker.getStyleClass().add("hue");

        this.slChooser.setPrefSize(gradientSize, gradientSize);
        this.slChooser.prefWidthProperty().bind(this.heightProperty());

        AnchorPane.setTopAnchor(this.transparencyPicker, -5.0);
        AnchorPane.setBottomAnchor(this.transparencyPicker, -5.0);
        AnchorPane.setLeftAnchor(this.transparencyPicker, 0.0);

        AnchorPane.setTopAnchor(this.huePicker, -5.0);
        AnchorPane.setBottomAnchor(this.huePicker, -5.0);
        AnchorPane.setRightAnchor(this.huePicker, 0.0);

        this.subControlsContainer.setPrefWidth(50.0);
        this.subControlsContainer.getChildren().addAll(this.transparencyPicker, this.huePicker);

        this.getChildren().addAll(slChooser, subControlsContainer);
        this.setSpacing(10.0);
        registerListeners();
    }

    private void registerListeners()
    {
        InvalidationListener updateColor = obs -> {
            Color color = Color.hsb(this.getHue(), this.getSaturation(), this.getLightness(), this.getTransparency());
            Color base = Color.hsb(this.getHue(), 1.0, 1.0, 1.0);
            this.slChooser.setBase(base);
            this.color.set(color);
        };
        this.hueProperty().addListener(updateColor);
        this.saturationProperty().addListener(updateColor);
        this.lightnessProperty().addListener(updateColor);
        this.transparencyProperty().addListener(updateColor);
    }
}
