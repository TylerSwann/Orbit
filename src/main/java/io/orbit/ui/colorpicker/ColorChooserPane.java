package io.orbit.ui.colorpicker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday August 25, 2018 at 16:35
 */
public class ColorChooserPane extends VBox
{
    private static final String DEFAULT_STYLE_CLASS = "color-chooser";

    private DirectionalColorPicker primaryPicker;
    private DirectionalColorPicker opacityPicker;
    private DirectionalColorPicker brightnessPicker;
    private OmniDirectionalColorPicker secondaryPicker;
    private AnchorPane pickerContainer;
    private VBox subControlsContainer;
    private Pane colorPreview;

    private static final double gradientSize = 250.0;

    public ColorChooserPane()
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.primaryPicker = new DirectionalColorPicker(DirectionalColorPicker.Orientation.VERTICAL);
        this.opacityPicker = new DirectionalColorPicker(DirectionalColorPicker.Orientation.HORIZONTAL);
        this.brightnessPicker = new DirectionalColorPicker(DirectionalColorPicker.Orientation.HORIZONTAL);
        this.secondaryPicker = new OmniDirectionalColorPicker(Color.RED);
        this.pickerContainer = new AnchorPane();
        this.subControlsContainer = new VBox();
        this.colorPreview = new Pane();
        build();
//        this.pickerContainer.setStyle("-fx-background-color: green;");
//        this.buttonContainer.setStyle("-fx-background-color: red;");
//        this.subControlsContainer.setStyle("-fx-background-color: blue;");
    }

    private void build()
    {
//        this.setPadding(new Insets(0.0, 20.0, 0.0, 20.0));
        this.setSpacing(10.0);
        this.setAlignment(Pos.TOP_CENTER);
        this.pickerContainer.setPrefHeight(gradientSize);
        this.secondaryPicker.setPrefSize(gradientSize, gradientSize);
        this.secondaryPicker.prefWidthProperty().bind(this.pickerContainer.heightProperty());
        this.primaryPicker.setPrefHeight(gradientSize);

        AnchorPane.setTopAnchor(this.secondaryPicker, 0.0);
        AnchorPane.setBottomAnchor(this.secondaryPicker, 0.0);
        AnchorPane.setLeftAnchor(this.secondaryPicker, 0.0);

        AnchorPane.setTopAnchor(this.primaryPicker, 0.0);
        AnchorPane.setBottomAnchor(this.primaryPicker, 0.0);
        AnchorPane.setRightAnchor(this.primaryPicker, 0.0);

        this.colorPreview.setPrefHeight(30.0);
        this.colorPreview.setStyle("-fx-background-color: pink;");

        buildSubControls();
        this.pickerContainer.getChildren().addAll(secondaryPicker, primaryPicker);
        this.getChildren().addAll(this.colorPreview, this.pickerContainer, this.subControlsContainer);
    }

    private void buildSubControls()
    {
        this.subControlsContainer.setAlignment(Pos.CENTER_LEFT);
        Label opacity = new Label("OPACITY: ");
        Label brightness = new Label("BRIGHTNESS: ");
        brightness.setPadding(new Insets(5.0, 0.0, 0.0, 0.0));
        List<Label> labels = Arrays.asList(opacity, brightness);
        labels.forEach(label -> {
            label.setTextFill(Color.valueOf("#303030"));
            label.setAlignment(Pos.CENTER_LEFT);
            label.setPrefWidth(this.getPrefWidth());
        });
        String gradient = "linear-gradient(to right, black 0%, white 100%)";
        this.opacityPicker.setGradient(gradient);
        this.brightnessPicker.setGradient(gradient);
        this.opacityPicker.setPrefHeight(15.0);
        this.brightnessPicker.setPrefHeight(15.0);
        this.subControlsContainer.getChildren().addAll(
                opacity,
                opacityPicker,
                brightness,
                brightnessPicker
        );
    }
}
