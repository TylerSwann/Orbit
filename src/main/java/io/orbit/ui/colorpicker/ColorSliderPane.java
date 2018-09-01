package io.orbit.ui.colorpicker;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Tyler Swann on Friday August 31, 2018 at 12:13
 */
public class ColorSliderPane extends AnchorPane
{
    private static final String DEFAULT_STYLE_CLASS = "color-slider-pane";
    private static final String RGBAOption = "RGBA Sliders";
    private static final String HSLOption = "HSL Sliders";

    enum ColorModel { RGBA, HSL }

    private JFXComboBox<String> colorModelBox;
    private VBox rgbaContainer;
    private VBox hslContainer;

    private TextFieldSlider redSlider;
    private TextFieldSlider greenSlider;
    private TextFieldSlider blueSlider;
    private TextFieldSlider alphaSlider;

    private TextFieldSlider hueSlider;
    private TextFieldSlider saturationSlider;
    private TextFieldSlider lightnessSlider;

    private ColorModel model = ColorModel.RGBA;
    private SimpleObjectProperty<Color> color = new SimpleObjectProperty<>();
    public Color getColor() { return color.get(); }
    public ObservableValue<Color> colorProperty() { return color; }

    public ColorSliderPane(Color color, ColorModel model)
    {
        this.color.set(color);
        this.model = model;
        create();
    }

    public ColorSliderPane()
    {
        this.color.set(Color.BLUE);
        create();
    }

    private void create()
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.colorModelBox = new JFXComboBox<>();
        this.rgbaContainer = new VBox();
        this.hslContainer = new VBox();

        this.redSlider = new TextFieldSlider("Red:", 0.0, 255.0, (this.getColor().getRed() * 255.0), true);
        this.greenSlider = new TextFieldSlider("Green:", 0.0, 255.0, (this.getColor().getGreen() * 255.0), true);
        this.blueSlider = new TextFieldSlider("Blue:", 0.0, 255.0, (this.getColor().getBlue() * 255.0), true);
        this.alphaSlider = new TextFieldSlider("Opacity:", 0.0, 1.0, (this.getColor().getOpacity()), false);

        this.hueSlider = new TextFieldSlider("Hue:", 0.0, 360.0, this.getColor().getHue(), true);
        this.saturationSlider = new TextFieldSlider("Saturation:", 0.0, 100.0, (this.getColor().getSaturation() * 100.0), true);
        this.lightnessSlider = new TextFieldSlider("Lightness:", 0.0, 100.0, (this.getColor().getBrightness() * 100.0), true);

        this.build();
        registerListeners();
    }

    private void build()
    {
        this.setPrefWidth(350.0);

        rgbaContainer.getChildren().addAll(
                this.redSlider,
                this.greenSlider,
                this.blueSlider,
                this.alphaSlider
        );

        hslContainer.getChildren().addAll(
                this.hueSlider,
                this.saturationSlider,
                this.lightnessSlider
        );

        VBox[] colorContainers = new VBox[2];
        colorContainers[0] = rgbaContainer;
        colorContainers[1] = hslContainer;
        for (VBox colorContainer : colorContainers)
        {
            AnchorPane.setLeftAnchor(colorContainer, 0.0);
            AnchorPane.setRightAnchor(colorContainer, 0.0);
            colorContainer.setAlignment(Pos.CENTER_LEFT);
        }

        colorModelBox.getItems().addAll(RGBAOption, HSLOption);
        colorModelBox.setValue(RGBAOption);
        AnchorPane.setTopAnchor(colorModelBox, 0.0);
        AnchorPane.setRightAnchor(colorModelBox, 0.0);
        AnchorPane.setLeftAnchor(colorModelBox, 0.0);
        this.getChildren().add(colorModelBox);

        switch (this.model)
        {
            case RGBA:
                this.getChildren().add(rgbaContainer);
                break;
            case HSL:
                this.getChildren().add(hslContainer);
                break;
            default: break;
        }
        this.colorModelBox.heightProperty().addListener(__ -> {
            AnchorPane.setTopAnchor(rgbaContainer, colorModelBox.getHeight() * 2);
            AnchorPane.setTopAnchor(hslContainer, colorModelBox.getHeight() * 2);
        });
        Platform.runLater(() -> {
            AnchorPane.setTopAnchor(rgbaContainer, colorModelBox.getHeight() * 2);
            AnchorPane.setTopAnchor(hslContainer, colorModelBox.getHeight() * 2);
        });
    }

    private void registerListeners()
    {
        colorModelBox.getSelectionModel().selectedItemProperty().addListener(__ -> {
            if (colorModelBox.getSelectionModel().getSelectedItem().equals(RGBAOption) && this.model != ColorModel.RGBA)
            {
                this.model = ColorModel.RGBA;
                this.getChildren().remove(hslContainer);
                this.getChildren().add(rgbaContainer);
            }
            else if (colorModelBox.getSelectionModel().getSelectedItem().equals(HSLOption) && this.model != ColorModel.HSL)
            {
                this.model = ColorModel.HSL;
                this.getChildren().remove(rgbaContainer);
                this.getChildren().add(hslContainer);
            }
        });
    }
}
