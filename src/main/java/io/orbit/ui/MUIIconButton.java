package io.orbit.ui;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Created by Tyler Swann on Sunday August 19, 2018 at 13:04
 */
public class MUIIconButton extends JFXButton
{
    private static final String DEFAULT_STYLE_CLASS = "mui-icon-button";
    private static final String ICON_STYLE_CLASS = "mui-icon";

    private FontIcon icon;

    public MUIIconButton(Ikon icon)
    {
        this.icon = new FontIcon(icon);
        this.icon.getStyleClass().add(ICON_STYLE_CLASS);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setGraphic(this.icon);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
}
