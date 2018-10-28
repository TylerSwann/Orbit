/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.orbit.ui.notification;

import com.jfoenix.controls.JFXSnackbar;
import io.orbit.App;
import io.orbit.api.Nullable;
import io.orbit.ui.notification.modal.MUIInputModal;
import io.orbit.ui.notification.modal.MUIModal;
import io.orbit.ui.notification.modal.MUIModalButton;
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


    public static void showSuccessNotification(String message)
    {
        MUINotification notification = MUINotification.success(message);
        notification.show(App.controller().container(), MUINotification.NotificationPos.BOTTOM_RIGHT);
    }

    public static void showErrorNotification(String message)
    {
        MUINotification notification = MUINotification.error(message);
        notification.show(App.controller().container(), MUINotification.NotificationPos.BOTTOM_RIGHT);
    }

    public static void showInfoNotification(String message)
    {
        MUINotification notification = MUINotification.informational(message);
        notification.show(App.controller().container(), MUINotification.NotificationPos.BOTTOM_RIGHT);
    }

    public static void showWarningNotification(String message)
    {
        MUINotification notification = MUINotification.warning(message);
        notification.show(App.controller().container(), MUINotification.NotificationPos.BOTTOM_RIGHT);
    }

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
        AnchorPane root = (AnchorPane) App.stage().getScene().getRoot();
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
        Stage owner = App.stage();
        modal.show(owner);
        double x = (owner.getX() + (owner.getWidth() / 2.0)) - modal.getWidth() / 2.0;
        double y = (owner.getY() + (owner.getHeight() / 2.0)) - modal.getHeight() / 2.0;
        modal.setX(x);
        modal.setY(y);
    }
}
