package io.orbit.ui.editorui;


import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class MUIGutterButton extends Label
{
    private static final String DEFAULT_STYLE_CLASS = "mui-gutter-button";

    public MUIGutterButton(int lineNumber)
    {
        super(String.valueOf(lineNumber));
        this.setPrefWidth(70.0);
        this.setPrefHeight(30.0);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setPadding(new Insets(0.0, 0.0, 0.0, 10.0));
    }
}
