package io.orbit.ui.colorpicker;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.tabs.MUITab;
import io.orbit.ui.tabs.MUITabBar;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Monday September 03, 2018 at 15:55
 */
public class MUIColorPicker extends AnchorPane
{
    private static final String DEFAULT_STYLE_CLASS = "mui-color-picker";
    private Pane colorPreview;
    private HSLColorPicker hslColorPicker;
    private ColorSliderPane sliderPane;
    private AnchorPane contentPane;
    private MUITabBar tabBar;
    private HBox buttonContainer;
    private VBox bottomContainer;
    private JFXButton choose;
    private JFXButton cancel;
    private Node selectedContent;

    private Consumer<Color> onChoose = __ -> {};
    public void setOnChoose(Consumer<Color> choose) { this.onChoose = onChoose; }
    public Consumer<Color> getOnChoose() { return onChoose; }

    private Runnable onCancel = () -> {};
    public void setOnCancel(Runnable cancel) { this.onCancel = cancel; }
    public Runnable getOnCancel() { return onCancel; }

    private ObjectProperty<Color> color = new SimpleObjectProperty<>();
    public Color getColor() { return color.get(); }
    public ObservableValue<Color> colorProperty() { return color; }

    public MUIColorPicker(Color color)
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.colorPreview = new Pane();
        this.tabBar = new MUITabBar();
        this.hslColorPicker = new HSLColorPicker(color);
        this.contentPane = new AnchorPane();
        this.choose = new JFXButton("CHOOSE");
        this.cancel = new JFXButton("CANCEL");
        this.choose.getStyleClass().add("mui-picker-button");
        this.cancel.getStyleClass().add("mui-picker-button");
        this.colorPreview.getStyleClass().add("color-preview");
        this.buttonContainer = new HBox();
        this.bottomContainer = new VBox();
        this.sliderPane = new ColorSliderPane();
        this.selectedContent = this.hslColorPicker;
        build();
        updateColor(color);
    }

    private void build()
    {
        this.setPrefWidth(330.0);
        this.setMinHeight(470.0);

        this.choose.setOnAction(__ -> this.onChoose.accept(this.getColor()));
        this.cancel.setOnAction(__ -> this.onCancel.run());
        this.buttonContainer.setAlignment(Pos.BOTTOM_CENTER);
        this.buttonContainer.getChildren().addAll(choose, cancel);
        this.buttonContainer.setPrefHeight(50.0);
        this.colorPreview.setPrefHeight(70.0);
        this.contentPane.setPrefHeight(250.0);
        this.sliderPane.setPadding(new Insets(0.0, 20.0, 0.0, 20.0));
        this.bottomContainer.setAlignment(Pos.BOTTOM_CENTER);

        MUITab pickerTab = new MUITab("Picker", new FontIcon(FontAwesomeSolid.CROSSHAIRS));
        MUITab slidersTab = new MUITab("Slider", new FontIcon(FontAwesomeSolid.SLIDERS_H));
        MUITab paletteTab = new MUITab("Palette", new FontIcon(FontAwesomeSolid.TH));
        MUITab[] tabs = new MUITab[] { pickerTab, slidersTab, paletteTab };
        Arrays.stream(tabs).forEach(tab -> tab.setContentDisplay(ContentDisplay.GRAPHIC_ONLY));

        this.tabBar.getTabs().add(pickerTab);
        this.tabBar.getTabs().add(slidersTab);
        this.tabBar.getTabs().add(paletteTab);
        Platform.runLater(() -> this.tabBar.select(pickerTab));

        applyAnchors(this.colorPreview, 0.0, null, 0.0, 0.0);
        applyAnchors(this.bottomContainer, null, 0.0, 0.0, 0.0);
        applyAnchors(this.hslColorPicker, 0.0, 0.0, 0.0, 0.0);
        applyAnchors(this.sliderPane, 0.0, 0.0, 0.0, 0.0);

        this.contentPane.getChildren().add(this.hslColorPicker);
        this.bottomContainer.getChildren().addAll(this.tabBar, this.buttonContainer);
        this.bottomContainer.setSpacing(5.0);
        this.getChildren().addAll(this.colorPreview, this.contentPane, this.bottomContainer);
        registerListeners();
    }

    private void registerListeners()
    {
        this.widthProperty().addListener(__ -> Platform.runLater(() -> {
            this.choose.setPrefSize(this.getWidth() / 2.0, this.buttonContainer.getHeight());
            this.cancel.setPrefSize(this.getWidth() / 2.0, this.buttonContainer.getHeight());
            this.bottomContainer.setPrefHeight(this.tabBar.getHeight() + this.buttonContainer.getHeight() + 5.0);
            this.tabBar.getTabs().forEach(tab -> tab.setPrefWidth(this.bottomContainer.getWidth() / 3.0));
            Platform.runLater(() -> applyAnchors(this.contentPane, this.colorPreview.getHeight() + 10.0, null, 0.0, 0.0));
        }));
        this.hslColorPicker.colorProperty().addListener(__ -> updateColor(this.hslColorPicker.getColor()));
        this.sliderPane.colorProperty().addListener(__ -> updateColor(this.sliderPane.getColor()));
        this.tabBar.selectedTabProperty().addListener(__ -> {
            switch (this.tabBar.getSelectedTab().getText())
            {
                case "Picker":
                    if (this.selectedContent != this.hslColorPicker)
                    {
                        this.contentPane.getChildren().remove(this.selectedContent);
                        this.contentPane.getChildren().add(this.hslColorPicker);
                        this.selectedContent = this.hslColorPicker;
                    }
                    break;
                case "Slider":
                    if (this.selectedContent != this.sliderPane)
                    {
                        this.sliderPane.setColor(this.getColor());
                        this.contentPane.getChildren().remove(this.selectedContent);
                        this.contentPane.getChildren().add(this.sliderPane);
                        this.selectedContent = this.sliderPane;
                    }
                    break;
                case "Palette":
                    break;
                default: break;
            }
        });
    }

    private void applyAnchors(Node node, Double top, Double bottom, Double left, Double right)
    {
        if (top != null)
            AnchorPane.setTopAnchor(node, top);
        if (bottom != null)
            AnchorPane.setBottomAnchor(node, bottom);
        if (left != null)
            AnchorPane.setLeftAnchor(node, left);
        if (right != null)
            AnchorPane.setRightAnchor(node, right);
    }

    private void updateColor(Color color)
    {
        this.color.setValue(color);
        this.colorPreview.setStyle(String.format("-fx-background-color: %s;", colorAsRgbaString()));
    }

    private String colorAsRgbaString()
    {
        return String.format("rgba(%d, %d, %d, %.5f)",
                Math.round(getColor().getRed() * 255.0),
                Math.round(getColor().getGreen() * 255.0),
                Math.round(getColor().getBlue() * 255.0),
                getColor().getOpacity()
        );
    }

    private String colorAsHslaString()
    {
        return String.format("hsla(%d, %d, %d, %.5f)",
                Math.round(getColor().getHue()),
                Math.round(getColor().getSaturation() * 100.0),
                Math.round(getColor().getBrightness() * 100.0),
                getColor().getOpacity()
        );
    }
}
