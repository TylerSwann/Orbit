package io.orbit.controllers;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSnackbar;
import io.orbit.App;
import io.orbit.api.NotNullable;
import io.orbit.api.Nullable;
import io.orbit.ui.MUIDialog;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Thursday April 12, 2018 at 14:25
 */
public class OStatusBarController
{
    private Label statusLabel;
    private JFXProgressBar progressBar;
    private JFXSnackbar snackbar;


    public OStatusBarController(AnchorPane container)
    {
        this.snackbar = new JFXSnackbar(container);
        this.statusLabel = new Label();
        this.progressBar = new JFXProgressBar();
        this.statusLabel.setAlignment(Pos.CENTER);
        this.progressBar.getStyleClass().add("mui-progress-bar");
        this.snackbar.getStyleClass().add("mui-snack-bar");
        this.statusLabel.setFont(new Font("Roboto Medium", 13.0));
        AnchorPane.setRightAnchor(this.statusLabel, 0.0);
        AnchorPane.setLeftAnchor(this.statusLabel, 0.0);
        AnchorPane.setBottomAnchor(this.statusLabel, 5.0);
        AnchorPane.setRightAnchor(this.progressBar, 0.0);
        AnchorPane.setLeftAnchor(this.progressBar, 0.0);
        AnchorPane.setBottomAnchor(this.progressBar, 0.0);
        container.getChildren().addAll(this.statusLabel, this.progressBar);
        this.setProgressBarHidden(true);
    }

    public void setStatus(String message)
    {
        this.statusLabel.setText(message);
    }

    public void setProgressIndeterminate(boolean indeterminate)
    {
        if (indeterminate)
            this.progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        else
            this.progressBar.setProgress(0.0);
    }

    public void setProgress(double progress)
    {
        this.progressBar.setProgress(progress);
    }

    public void showSnackBarMessage(String message, long timeout)
    {
        this.snackbar.show(message, timeout);
    }

    private void setProgressBarHidden(boolean hidden)
    {
        if (hidden)
            this.progressBar.setOpacity(0.0);
        else this.progressBar.setOpacity(1.0);
    }

    public void showDialogMessage(String title, String message, @Nullable Runnable onConfirm, @NotNullable Runnable onCancel)
    {
        MUIDialog dialog = new MUIDialog(title, "Cancel", "Ok", message);
        dialog.setOnPrimaryClick(input -> {
            if (onConfirm != null)
                onConfirm.run();
        });
        if (onCancel != null)
            dialog.setOnSecondaryClick(onCancel);
        dialog.show(App.applicationController().rootSplitPane);
    }

    public void showDialogMessage(String title, String message, String promptText, @Nullable Consumer<Optional<String>> onConfirm, @NotNullable Runnable onCancel)
    {
        MUIDialog dialog = new MUIDialog(title, "Cancel", "Ok", message, promptText);
        if (onConfirm != null)
            dialog.setOnPrimaryClick(onConfirm);
        if (onCancel != null)
            dialog.setOnSecondaryClick(onCancel);
        dialog.show(App.applicationController().rootSplitPane);
    }

    public void showConfirmDeleteMessage(String title, String message, @Nullable Consumer<Optional<String>> onConfirm, @NotNullable Runnable onCancel)
    {
        MUIDialog dialog = new MUIDialog(title, "Cancel", "DELETE", message);
        if (onConfirm != null)
            dialog.setOnPrimaryClick(onConfirm);
        if (onCancel != null)
            dialog.setOnSecondaryClick(onCancel);
        dialog.show(App.applicationController().rootSplitPane);
    }
}
