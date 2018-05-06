package io.orbit.controllers;

import com.jfoenix.controls.JFXComboBox;
import io.orbit.App;
import io.orbit.ui.MUIButton;
import io.orbit.util.Setting;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Friday February 23, 2018 at 15:17
 */
public class OSettingsController
{
    private static SettingsWindow settingsWindow;

    public OSettingsController() {}

    void bind(Button settingsButton)
    {
        List<ColorSetting<ApplicationColorSetting>> appColorSettings = new ArrayList<>();
        for (int i = 0; i < ApplicationColorSetting.values().length; i++)
        {
            ApplicationColorSetting setting = ApplicationColorSetting.values()[i];
            appColorSettings.add(new ColorSetting<>(setting, Color.BLACK));
        }
        ColorSettingsPage<ApplicationColorSetting> appColorSettingsPage = new ColorSettingsPage<>(appColorSettings, getThemeChooser(), new Pane());
        Setting editorColorSettingsParent = new Setting("Editor Color Settings", new Pane());
        Setting appColorSettingsParent = new Setting("Application Color Settings", appColorSettingsPage);
        Setting colorSettings = new Setting("Colors", new Setting[] {
                appColorSettingsParent,
                editorColorSettingsParent
        });
        settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (settingsWindow == null)
                settingsWindow = new SettingsWindow(new Setting[]{ colorSettings });
            settingsWindow.show();
        });
    }

    private HBox getThemeChooser()
    {
        HBox container = new HBox();
        applyAnchors(container);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefHeight(100.0);
        container.setPadding(new Insets(0.0, 0.0, 0.0, 20.0));
        container.setSpacing(20.0);
        JFXComboBox<String> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll("Default", "Material Dark");
        MUIButton apply = new MUIButton("APPLY");
        apply.setOnAction(event -> {
            if (comboBox.getValue() != null && comboBox.getValue().equals("Material Dark"))
            {
                App.setApplicationTheme("css/MaterialDark.css");
                App.setSyntaxTheme("css/MaterialDarkSyntax.css");
            }
        });
        container.getChildren().addAll(comboBox, apply);
        return container;
    }

    private class ColorSettingsPage<T extends ColorSettingEnumerable> extends VBox
    {
        final VBox labelContainer = new VBox();
        final VBox colorPickerContainer = new VBox();
        final HBox container = new HBox();

        public ColorSettingsPage(List<ColorSetting<T>> colorSettings, Node top, Node bottom)
        {
            this(colorSettings);
            this.getChildren().add(0, top);
            this.getChildren().add(this.getChildren().size() - 1, bottom);
        }
        /**
         *
         * @param colorSettings - Array of ColorSetting<T extends ColorSettingEnumerable>
         */
        public ColorSettingsPage(List<ColorSetting<T>> colorSettings)
        {
            labelContainer.setAlignment(Pos.CENTER_LEFT);
            colorPickerContainer.setAlignment(Pos.CENTER_RIGHT);
            this.container.getChildren().addAll(labelContainer, colorPickerContainer);
            labelContainer.setSpacing(5.0);
            colorPickerContainer.setSpacing(5.0);
            this.container.setAlignment(Pos.CENTER_LEFT);
            this.container.setSpacing(50.0);
            this.container.setPadding(new Insets(0.0, 0.0, 0.0, 20.0));
            AnchorPane.setTopAnchor(this.container, 50.0);
            AnchorPane.setBottomAnchor(this.container, 50.0);
            AnchorPane.setLeftAnchor(this.container, 0.0);
            AnchorPane.setRightAnchor(this.container, 0.0);

            applyAnchors(this);

            for (ColorSetting setting : colorSettings)
            {
                Label label = new Label(setting.colorSettingName);
                label.setPrefHeight(25.0);
                label.getStyleClass().add("config-label");
                ColorPicker colorPicker = new ColorPicker(setting.defaultColor);
                labelContainer.getChildren().add(label);
                colorPickerContainer.getChildren().add(colorPicker);
            }
            this.getChildren().add(this.container);
        }
    }

    private void applyAnchors(Node node)
    {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    private class ColorSetting<T extends ColorSettingEnumerable>
    {
        final String colorSettingName;
        final Color defaultColor;
        final T setting;

        ColorSetting(T setting, Color defaultColor)
        {
            this.colorSettingName = setting.getSettingName();
            this.defaultColor = defaultColor;
            this.setting = setting;
        }
    }

    private enum SettingType
    {
        APPLICATION_COLORS,
        EDITOR_COLORS
    }

    private enum ApplicationColorSetting implements ColorSettingEnumerable
    {
        MENU_BAR_COLOR("Menu Bar Color"),
        MENU_BAR_TEXT_COLOR("Menu Bar Text Color"),
        CONTEXT_MENU_BACKGROUND_COLOR("Context Menu Background Color"),
        CONTEXT_MENU_FONT_COLOR("Context Menu Font Color"),
        MENU_BAR_RIPPLE_COLOR("Menu Bar Ripple"),
        TAB_PANE_BACKGROUND_COLOR("Tab Pane Background Color"),
        TAB_PANE_RIPPLE_COLOR("Tab Pane Ripple Color"),
        TAB_PANE_LINE_COLOR("Tab Pane Line Color"),
        TAB_PANE_TEXT_COLOR("Tab Pane Text Color"),
        VERTICAL_SPLIT_PANE_DIV_COLOR("Vertical Split Pane Divider Color"),
        HORIZONTAL_SPLIT_PANE_DIV_COLOR("Horizontal Split Pane Divider Color"),
        TERMINAL_BACKGROUND_COLOR("Terminal Background Color"),
        TERMINAL_TEXT_COLOR("Terminal Text Color"),
        TREE_VIEW_BACKGROUND_COLOR("Tree View Background Color"),
        TREE_VIEW_ITEM_COLOR("Tree View Item Color"),
        TREE_VIEW_ITEM_COLOR_SELECTED("Tree View Selected Item Color"),
        TREE_VIEW_ITEM_TEXT_COLOR("Tree View item Text Color"),
        NAV_BAR_BACKGROUND_COLOR("Navigation Bar Background Color"),
        NAV_BAR_ITEM_RIPPLE_COLOR("Navigation Bar Item Ripple Color"),
        NAV_BAR_ITEM_TEXT_COLOR("Navigation Bar Item Text Color");

        private final String settingName;

        @Override
        public String getSettingName() { return this.settingName; }

        ApplicationColorSetting(String settingName)
        {
            this.settingName = settingName;
        }
    }

    private interface ColorSettingEnumerable
    {
        String getSettingName();
    }
}
