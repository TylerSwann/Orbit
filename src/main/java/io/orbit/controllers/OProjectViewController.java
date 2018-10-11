package io.orbit.controllers;

import io.orbit.App;
import io.orbit.api.notification.Notifications;
import io.orbit.api.notification.modal.MUIInputModal;
import io.orbit.api.notification.modal.MUIModal;
import io.orbit.api.notification.modal.MUIModalButton;
import io.orbit.settings.LocalUser;
import io.orbit.api.event.FileTreeMenuEvent;
//import io.orbit.ui.navigator.MUIFileTreeView;
import io.orbit.ui.treeview.MUIFileTreeView2;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Tyler Swann on Saturday July 21, 2018 at 15:39
 */
public class OProjectViewController
{
    private MUIFileTreeView2 projectView;
    private List<File> fileClipboard = new ArrayList<>();
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private Mode mode = Mode.NONE;

    private enum Mode { CUT, COPY, NONE }
    
    public OProjectViewController(MUIFileTreeView2 projectView)
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
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FILE_TYPE, this::newFile);
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_FOLDER, this::newFolder);
        this.projectView.addEventHandler(FileTreeMenuEvent.NEW_PROJECT, this::newProject);
        this.projectView.addEventHandler(FileTreeMenuEvent.ITEM_DOUBLE_CLICK, this::openFile);
    }


    private void openFile(FileTreeMenuEvent event)
    {
        event.getSelectedFiles().forEach(file -> {
            if (file.isDirectory())
                return;
            App.controller().getEditorTabPaneController().openFile(file);
        });
    }

    private void cut(FileTreeMenuEvent event)
    {
        this.fileClipboard = event.getSelectedFiles();
        this.mode = Mode.CUT;
    }

    private void copy(FileTreeMenuEvent event)
    {
        this.fileClipboard = event.getSelectedFiles();
        this.mode = Mode.COPY;
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
        File root = LocalUser.settings.getLastModifiedProject().getProjectRoot();
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
        if (this.fileClipboard.isEmpty() || event.getSelectedFiles().size() < 1 || this.mode == Mode.NONE)
            return;
        boolean success = true;
        boolean copy = this.mode == Mode.COPY;
        success = this.relocateFiles(this.fileClipboard, event.getSelectedFiles().get(0), copy);
        if (!success)
            Notifications.showErrorAlert("Oops", "Sorry, we ran into a problem and were unable to move/copy some files!");
        App.controller().getProjectNavigatorController().removeFiles(this.fileClipboard.toArray(new File[this.fileClipboard.size()]));
        this.fileClipboard.clear();
        this.mode = Mode.NONE;
    }

    private void delete(FileTreeMenuEvent event)
    {
        MUIModalButton cancel = new MUIModalButton("CANCEL", MUIModalButton.MUIModalButtonStyle.SECONDARY);
        MUIModalButton delete = new MUIModalButton("DELETE", MUIModalButton.MUIModalButtonStyle.DESTRUCTIVE);
        MUIModal modal = new MUIModal("Delete", "Are you sure you want to delete the selected file(s)?", cancel, delete);

        delete.setOnAction(__ -> {
            boolean allDeleted = true;

            for (File file : event.getSelectedFiles())
            {
                try
                {
                    if (file.isDirectory())
                        FileUtils.deleteDirectory(file);
                    else
                    {
                        boolean deleted = file.delete();
                        allDeleted = deleted && allDeleted;
                    }
                    App.controller().getProjectNavigatorController().removeFiles(file);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    allDeleted = false;
                }
            }
            if (!allDeleted)
                Notifications.showErrorAlert("Oops", "Sorry, we ran into a problem and were unable to delete some of the files!");

        });
        Notifications.showModal(modal);
    }

    private void newFile(FileTreeMenuEvent event)
    {
        if (event.getFileType() != null)
            IOController.showFileCreationDialog(event.getFileType(), getTargetFile(event));
        else
            IOController.showFileCreationDialog(getTargetFile(event));
    }

    private void newFolder(FileTreeMenuEvent event) { IOController.showDirectoryCreationDialog(getTargetFile(event)); }

    private File getTargetFile(FileTreeMenuEvent event)
    {
        if (event.getSelectedFiles().size() <= 0)
            throw new RuntimeException("Selected files from FileTreeMenuEvent is null");
        File targetFile = event.getSelectedFiles().get(0);
        return targetFile.isDirectory() ? targetFile : targetFile.getParentFile();
    }

    private boolean relocateFiles(List<File> from, File to, boolean copy)
    {
        boolean movedAll = true;
        File destFolder = to.isDirectory() ? to : to.getParentFile();
        for (File file : from)
        {
            if (!file.exists())
                continue;
            try
            {
                String destinationPath = String.format("%s\\%s", destFolder.getPath(), file.getName());
                if (copy)
                    Files.copy(Paths.get(file.getPath()), Paths.get(destinationPath));
                else
                    Files.move(Paths.get(file.getPath()), Paths.get(destinationPath));
            }
            catch (IOException ex)
            {
                movedAll = false;
                ex.printStackTrace();
            }
        }
        return movedAll;
    }

    private void newProject(FileTreeMenuEvent event) { }
}
