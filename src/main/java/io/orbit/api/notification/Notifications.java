package io.orbit.api.notification;

import com.jfoenix.controls.JFXSnackbar;
import io.orbit.api.Nullable;
import io.orbit.api.OrbitApplication;
import io.orbit.api.notification.modal.MUIInputModal;
import io.orbit.api.notification.modal.MUIModal;
import io.orbit.api.notification.modal.MUIModalButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 14:59
 */
public final class Notifications
{
    private Notifications(){}


    public static void showErrorAlert(String title, String message)
    {
        MUIModalButton ok = new MUIModalButton("OK", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIModal modal = new MUIModal(title, message, FontAwesomeSolid.EXCLAMATION_CIRCLE, ok);
        modal.getIcon().getStyleClass().add("error-icon");
        showModal(modal);
    }

    public static void showWarningAlert(String title, String message)
    {
        MUIModalButton ok = new MUIModalButton("OK", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIModal modal = new MUIModal(title, message, FontAwesomeSolid.EXCLAMATION_TRIANGLE, ok);
        modal.getIcon().getStyleClass().add("warning-icon");
        showModal(modal);
    }

    public static void showInputModal(String title, String message, @Nullable String promptText, @Nullable Consumer<String> onTextChange, MUIModalButton... buttons)
    {
        MUIInputModal modal = new MUIInputModal(title, message, buttons);
        if (promptText != null)
            modal.setPromptText(promptText);
        if (onTextChange != null)
            modal.setOnTextChange(onTextChange);
        showModal(modal);
    }

    public static void showModal(String message, String title, MUIModalButton... buttons)
    {
        MUIModal modal = new MUIModal(title, message, buttons);
        showModal(modal);
    }

    public static void showSnackBarMessage(String message)
    {
        showSnackBarMessage(message, 3000);
    }
    public static void showSnackBarMessage(String message, long timeout)
    {
        AnchorPane root = (AnchorPane) OrbitApplication.PRIMARY_STAGE.getValue().getScene().getRoot();
        JFXSnackbar snackBar = new JFXSnackbar(root);
        snackBar.show(message, timeout);
    }

    /**
     *
     * Positions the modal to the center of the primary stage
     * @param modal - An instance of MUIModal
     */
    public static void showModal(MUIModal modal)
    {
        Stage owner = OrbitApplication.PRIMARY_STAGE.getValue();
        modal.show(OrbitApplication.PRIMARY_STAGE.getValue());
        double x = (owner.getX() + (owner.getWidth() / 2.0)) - modal.getWidth() / 2.0;
        double y = (owner.getY() + (owner.getHeight() / 2.0)) - modal.getHeight() / 2.0;
        modal.setX(x);
        modal.setY(y);
    }
}
