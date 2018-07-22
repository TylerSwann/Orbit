package io.orbit.controllers;

import io.orbit.api.notification.Notifications;
import io.orbit.api.notification.modal.MUIInputModal;
import io.orbit.api.notification.modal.MUIModalButton;
import io.orbit.settings.LocalUser;
import io.orbit.ui.navigator.FileTreeMenuEvent;
import io.orbit.ui.navigator.MUIFileTreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler Swann on Saturday July 21, 2018 at 15:39
 */
public class OProjectViewController
{
    private MUIFileTreeView projectView;
    private List<File> fileClipboard = new ArrayList<>();
    private Clipboard clipboard = Clipboard.getSystemClipboard();

    private enum Mode { CUT, COPY, PASTE }
    
    public OProjectViewController(MUIFileTreeView projectView)
    {
        this.projectView = projectView;
        this.registerListeners();
    }
    
    private void registerListeners()
    {
        this.projectView.addEventHandler(FileTreeMenuEvent.CUT, this::cut);
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY, this::copy);
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY_PATH, this::copyPath);
        this.projectView.addEventHandler(FileTreeMenuEvent.COPY_RELATIVE_PATH, this::copyRelativePath);
        this.projectView.addEventHandler(FileTreeMenuEvent.PASTE, this::paste);
        this.projectView.addEventHandler(FileTreeMenuEvent.DELETE, this::delete);
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FILE, this::newFile);
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FOLDER, this::newFolder);
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_PROJECT, this::newProject);
    }
    private void cut(FileTreeMenuEvent event)
    {
        this.fileClipboard = event.getSelectedFiles();
    }

    private void copy(FileTreeMenuEvent event)
    {
        this.fileClipboard = event.getSelectedFiles();
    }

    private void copyPath(FileTreeMenuEvent event)
    {
        StringBuffer buffer = new StringBuffer("");
        event.getSelectedFiles().forEach(file -> buffer.append(String.format("%s\n", file.getPath())));
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, buffer.toString());
        this.clipboard.setContent(content);
    }

    private void copyRelativePath(FileTreeMenuEvent event)
    {
        File root = LocalUser.userSettings.getLastModifiedProject().getProjectRoot();
        StringBuffer buffer = new StringBuffer("");

        event.getSelectedFiles().forEach(file -> {
            String relativePath = String.format("%s\n", file.getPath().replace(root.getPath(), ""));
            if (relativePath.length() > 0)
            {
                char firstChar = relativePath.toCharArray()[0];
                if (firstChar == '\\')
                    relativePath = relativePath.substring(1, relativePath.length() - 1);
            }
            buffer.append(relativePath);
        });
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, buffer.toString());
        this.clipboard.setContent(content);
    }

    private void paste(FileTreeMenuEvent event)
    {

    }

    private void delete(FileTreeMenuEvent event)
    {

    }
    private void newFile(FileTreeMenuEvent event)
    {
        MUIModalButton cancel = new MUIModalButton("CANCEL", MUIModalButton.MUIModalButtonStyle.SECONDARY);
        MUIModalButton create = new MUIModalButton("CREATE", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIInputModal modal = new MUIInputModal("Create File", "Enter a new file name:", cancel, create);
        Notifications.showModal(modal);
    }
    private void newFolder(FileTreeMenuEvent event)
    {
        MUIModalButton cancel = new MUIModalButton("CANCEL", MUIModalButton.MUIModalButtonStyle.SECONDARY);
        MUIModalButton create = new MUIModalButton("CREATE", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIInputModal modal = new MUIInputModal("Create Directory", "Enter a new directory name:", cancel, create);
        Notifications.showModal(modal);
    }

    private void newProject(FileTreeMenuEvent event)
    {

    }

}
