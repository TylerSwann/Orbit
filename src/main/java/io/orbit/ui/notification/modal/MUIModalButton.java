package io.orbit.ui.notification.modal;

import com.jfoenix.controls.JFXButton;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 18:54
 */
public class MUIModalButton extends JFXButton
{
    public enum MUIModalButtonStyle
    {
        PRIMARY,
        SECONDARY,
        DESTRUCTIVE

    }
    private static final String DEFAULT_STYLE_CLASS = "mui-modal-button";

    public MUIModalButton(String text, MUIModalButtonStyle style)
    {
        super(text);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        String className;
        switch (style)
        {
            case PRIMARY:
                className = "primary";
                break;
            case SECONDARY:
                className = "secondary";
                break;
            case DESTRUCTIVE:
                className = "destructive";
                break;
            default:
                className = "";
                break;
        }
        this.getStyleClass().add(className);
        if (style == MUIModalButtonStyle.DESTRUCTIVE)
        {
            FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH_ALT);
            this.setGraphic(deleteIcon);
        }
    }
}
