package io.orbit.controllers;


import io.orbit.App;
import io.orbit.api.Nullable;
import io.orbit.api.notification.Notifications;
import io.orbit.api.notification.modal.MUIInputModal;
import io.orbit.api.notification.modal.MUIModalButton;
import io.orbit.api.text.FileType;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class IOController
{
    private static final String FOLDER_NAME_PATTERN = "^[\\w \\-_]+$";
    private static final String FILE_NAME_PATTERN = "^[\\w _-]+\\.\\w+$";

    private IOController(){}


    public static void showFileCreationDialog(File parent)
    {
        showFileCreationDialog("New File", "Name: ", null, parent);
    }

    public static void showFileCreationDialog(FileType fileType, File parent)
    {
        showFileCreationDialog(String.format("New %s", fileType.getDisplayText()), "Name: ", fileType.getExtension(), parent);
    }

    public static void showFileCreationDialog(String title, String message, @Nullable String expectedFileExtension, File parent)
    {
        BiConsumer<ActionEvent, String> onCreate = (event, input) -> {
            String fileName = input;
            if (!fileName.matches(FILE_NAME_PATTERN) && expectedFileExtension != null)
                fileName = String.format("%s.%s", fileName, expectedFileExtension);
            if (!checkIsValidFileName(fileName))
                return;
            File file = new File(String.format("%s\\%s", parent.getPath(), fileName));
            validateAndCreate(file, false);
        };
        showCreationDialog(title, message, onCreate);
    }

    public static void showDirectoryCreationDialog(File parent)
    {
        showCreationDialog("New Directory", "Name: ", (event, input) -> {
            if (!checkIsValidFolderName(input))
                return;
            File folder = new File(String.format("%s\\%s", parent.getPath(), input));
            validateAndCreate(folder, true);
        });
    }

    private static void showCreationDialog(String title, String message, BiConsumer<ActionEvent, String> onCreate)
    {
        MUIModalButton cancel = new MUIModalButton("CANCEL", MUIModalButton.MUIModalButtonStyle.SECONDARY);
        MUIModalButton create = new MUIModalButton("CREATE", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIInputModal modal = new MUIInputModal(title, message, cancel, create);
        Notifications.showModal(modal);
        create.setOnAction(event -> onCreate.accept(event, modal.getText()));
    }

    private static boolean checkIsValidFileName(String name)
    {
        boolean isValid;
        if (name == null || name.isEmpty())
            isValid = false;
        else
            isValid = name.matches(FILE_NAME_PATTERN);

        if (!isValid)
            showInvalidNameAlert(false);
        return isValid;
    }

    private static void validateAndCreate(File file, boolean isDirectory)
    {
        String type = isDirectory ? "Directory" : "File";
        if (file.exists())
        {
            Notifications.showErrorAlert(String.format("%s Already Exists!", type), String.format("Sorry, a %s with that name already exists!", type.toLowerCase()));
            return;
        }
        boolean success = false;
        try
        {
            if (isDirectory)
                success = file.mkdir();
            else
                success = file.createNewFile();
        }
        catch (IOException e) { e.printStackTrace(); }
        if (!success)
            Notifications.showErrorAlert("Oops", String.format("Sorry, there was a problem and were unable to create that %s", type.toLowerCase()));
        App.controller().getProjectNavigatorController().forceRefresh();
    }

    private static boolean checkIsValidFolderName(String name)
    {
        boolean isValid;
        if (name == null || name.isEmpty())
            isValid = false;
        isValid = name.matches(FOLDER_NAME_PATTERN);
        if (!isValid)
            showInvalidNameAlert(true);
        return isValid;
    }

    private static void showInvalidNameAlert(boolean isDirectory)
    {
        String type = isDirectory ? "directory" : "file";
        Notifications.showErrorAlert("Invalid Name", String.format("Sorry, that is not a valid %s name", type));
    }
}
