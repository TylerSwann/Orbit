package io.orbit.ui.colorpicker;

import com.jfoenix.controls.JFXButton;
import io.orbit.ui.tabs.MUITab;
import io.orbit.ui.tabs.MUITabPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;

/**
 * Created by Tyler Swann on Friday August 31, 2018 at 17:49
 */
public class MUIColorPicker extends AnchorPane
{
    private static final String DEFAULT_STYLE_CLASS = "mui-color-picker";

    private MUITabPane tabPane;
    private ColorChooserPane chooserPane;
    private ColorSliderPane sliderPane;
    private HBox buttonContainer;
    private JFXButton choose;
    private JFXButton cancel;

    public MUIColorPicker()
    {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.buttonContainer = new HBox();
        this.choose = new JFXButton("CHOOSE");
        this.cancel = new JFXButton("CANCEL");
        this.choose.getStyleClass().add("mui-picker-button");
        this.cancel.getStyleClass().add("mui-picker-button");
        this.tabPane = new MUITabPane();
        this.chooserPane = new ColorChooserPane();
        this.sliderPane = new ColorSliderPane();
        build();
    }

    private void build()
    {
        this.setPrefWidth(350.0);
        this.setMinHeight(550.0);
        this.choose.setButtonType(JFXButton.ButtonType.RAISED);
        this.cancel.setButtonType(JFXButton.ButtonType.RAISED);
        this.buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        this.buttonContainer.setSpacing(15.0);
        this.buttonContainer.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
        this.buttonContainer.getChildren().addAll(choose, cancel);

        MUITab pickerTab = new MUITab("Picker", new FontIcon(FontAwesomeSolid.CROSSHAIRS));
        MUITab slidersTab = new MUITab("Sliders", new FontIcon(FontAwesomeSolid.SLIDERS_H));
        MUITab paletteTab = new MUITab("Palette", new FontIcon(FontAwesomeSolid.TH));
        MUITab[] tabs = new MUITab[] { pickerTab, slidersTab, paletteTab };
        Arrays.stream(tabs).forEach(tab -> tab.setContentDisplay(ContentDisplay.GRAPHIC_ONLY));

        pickerTab.setContent(this.chooserPane);
        slidersTab.setContent(this.sliderPane);
        paletteTab.setContent(new Pane());

        this.tabPane.getTabs().add(slidersTab);
        this.tabPane.getTabs().add(pickerTab);
        this.tabPane.getTabs().add(paletteTab);

        AnchorPane.setTopAnchor(this.tabPane, 0.0);
        AnchorPane.setLeftAnchor(this.tabPane, 0.0);
        AnchorPane.setRightAnchor(this.tabPane, 0.0);

        AnchorPane.setBottomAnchor(this.buttonContainer, 0.0);
        AnchorPane.setLeftAnchor(this.buttonContainer, 0.0);
        AnchorPane.setRightAnchor(this.buttonContainer, 0.0);
        Platform.runLater(() -> {
            AnchorPane.setBottomAnchor(this.tabPane, this.buttonContainer.getHeight());
        });
        this.getChildren().addAll(this.tabPane, this.buttonContainer);
    }
}
