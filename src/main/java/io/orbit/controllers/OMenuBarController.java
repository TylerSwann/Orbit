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
package io.orbit.controllers;

import io.orbit.App;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.ui.notification.Notifications;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.text.FileType;
import io.orbit.controllers.events.MenuBarEvent;
import io.orbit.settings.*;
import io.orbit.ui.menubar.ApplicationMenuBar;
import io.orbit.ui.menubar.SystemMenuBar;
import io.orbit.util.OS;
import io.orbit.util.EventTargetObject;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Optional;

/**
 * Created by Tyler Swann on Sunday July 15, 2018 at 15:39
 */
public class OMenuBarController extends EventTargetObject
{
    private SystemMenuBar menuBar;
    private Optional<CodeEditor> activeEditor;


    public OMenuBarController(AnchorPane container)
    {
        if (OS.isMacOS)
        {
            //TODO - Create macOS specific menu bar
        }
        else
        {
            ApplicationMenuBar menuBar = new ApplicationMenuBar();
            AnchorPane.setTopAnchor(menuBar, 0.0);
            AnchorPane.setLeftAnchor(menuBar, 0.0);
            AnchorPane.setRightAnchor(menuBar, 0.0);
            container.getChildren().add(menuBar);
            this.menuBar = menuBar;
        }
        OCodeEditorController editorController = OEditorTabPaneController.ACTIVE_EDITOR_CONTROLLER.getValue();
        if (editorController != null)
            this.activeEditor = Optional.of(editorController.getEditor());
        else
            this.activeEditor = Optional.empty();
        this.setEventHandlers();
        this.registerListeners();
    }

    private void setEventHandlers()
    {
        this.menuBar.setOnViewTerminal(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.VIEW_TERMINAL)));
        this.menuBar.setOnViewNavigator(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.VIEW_NAVIGATOR)));
        this.menuBar.setOnNewFile(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.NEW_FILE)));
        this.menuBar.setOnNewFolder(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.NEW_FOLDER)));
        this.menuBar.setOnNewProject(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.NEW_PROJECT)));
        this.menuBar.setOnSave(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.SAVE)));
        this.menuBar.setOnSaveAll(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.SAVE_ALL)));
        this.menuBar.setOnOpenFile(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.OPEN_FILE)));
        this.menuBar.setOnOpenFolder(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.OPEN_FOLDER)));
        this.menuBar.setOnSettings(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.SETTINGS)));
        this.menuBar.setOnUndo(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.UNDO)));
        this.menuBar.setOnRedo(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.REDO)));
        this.menuBar.setOnCut(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.CUT)));
        this.menuBar.setOnCopy(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.COPY)));
        this.menuBar.setOnPaste(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.PASTE)));
        this.menuBar.setOnFind(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.FIND)));
        this.menuBar.setOnSelectAll(__ -> this.fireEvent(new MenuBarEvent(this, this, MenuBarEvent.SELECT_ALL)));
        this.menuBar.setOnNewCustomFileType((e, fileType) -> this.fireEvent(new MenuBarEvent(MenuBarEvent.NEW_CUSTOM_FILE_TYPE, ((FileType)fileType))));
        this.menuBar.setOnVisitHomePage((e) -> {});
        this.menuBar.setOnViewLicense((e) -> Help.showLicense());
        this.menuBar.setOnAbout((e) -> Help.showAboutPage());
    }

    private void registerListeners()
    {
        OEditorTabPaneController.ACTIVE_EDITOR_CONTROLLER.addListener(__ -> {
            OCodeEditorController editorController = OEditorTabPaneController.ACTIVE_EDITOR_CONTROLLER.getValue();
            if (editorController != null)
                this.activeEditor = Optional.of(editorController.getEditor());
            else
                this.activeEditor = Optional.empty();
        });
        this.addEventHandler(MenuBarEvent.FIND, event -> App.controller().getActiveEditor().ifPresent(editor -> editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.FIND, editor.getFile()))));
        this.addEventHandler(MenuBarEvent.UNDO, event -> App.controller().getActiveEditor().ifPresent(CodeEditor::undo));
        this.addEventHandler(MenuBarEvent.REDO, event -> App.controller().getActiveEditor().ifPresent(CodeEditor::redo));
        this.addEventHandler(MenuBarEvent.SAVE, this::saveFile);
        this.addEventHandler(MenuBarEvent.SAVE_ALL, event -> this.saveAll());
        this.addEventHandler(MenuBarEvent.SELECT_ALL, event -> App.controller().getActiveEditor().ifPresent(CodeEditor::selectAll));
        this.addEventHandler(MenuBarEvent.SETTINGS, event -> Platform.runLater(SettingsPage::show));
        this.addEventHandler(MenuBarEvent.NEW_PROJECT, event -> {});
        this.addEventHandler(MenuBarEvent.OPEN_FILE, event -> this.showFileChooserDialog());
        this.addEventHandler(MenuBarEvent.OPEN_FOLDER, event -> this.showFolderChooseDialog());
        this.addEventHandler(MenuBarEvent.NEW_FILE, event -> showFileCreationDialog(false));
        this.addEventHandler(MenuBarEvent.NEW_FOLDER, event -> showFileCreationDialog(true));
        this.addEventHandler(MenuBarEvent.CUT, __ -> activeEditor.ifPresent(CodeEditor::cut));
        this.addEventHandler(MenuBarEvent.COPY, __ -> activeEditor.ifPresent(CodeEditor::copy));
        this.addEventHandler(MenuBarEvent.PASTE, __ -> activeEditor.ifPresent(CodeEditor::paste));
        this.addEventHandler(MenuBarEvent.NEW_CUSTOM_FILE_TYPE, event -> event.getFileType().ifPresent(this::showFileCreationDialog));
    }

    private void showFileCreationDialog(FileType fileType) { IOController.showFileCreationDialog(fileType, LocalUser.project.getProjectRoot()); }

    private void showFileCreationDialog(boolean isDirectory)
    {
        if (isDirectory)
            IOController.showDirectoryCreationDialog(LocalUser.project.getProjectRoot());
        else
            IOController.showFileCreationDialog(LocalUser.project.getProjectRoot());
    }

    private void saveFile(MenuBarEvent event)
    {
        boolean saved = true;
        Optional<ProjectFile> file = App.controller().getActiveProjectFile();
        saved = file.map(ProjectFile::save).orElse(false);
        if (saved)
            Notifications.showSnackBarMessage(String.format("Saved %s!", file.get().getName()), 2000);
        else
            Notifications.showSnackBarMessage("ERROR We were unable to save that file!", 2000);
    }

    private void saveAll()
    {
        boolean savedAll = true;
        for (ProjectFile file : App.controller().getActiveProjectFiles())
        {
            boolean success = file.save();
            savedAll = savedAll && success;
        }
        if (savedAll)
            Notifications.showSnackBarMessage("Saved All!", 2000);
        else
            Notifications.showSnackBarMessage("ERROR Saving Some Files!", 3000);
    }

    private void showFileChooserDialog()
    {
        FileChooser chooser = new FileChooser();
        File selectedFile = chooser.showOpenDialog(this.menuBar.getScene().getWindow());
        if (selectedFile != null && selectedFile.exists())
            this.fireEvent(new MenuBarEvent(MenuBarEvent.OPEN_FILE, selectedFile));
    }

    private void showFolderChooseDialog()
    {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Folder");
        File root = chooser.showDialog(this.menuBar.getScene().getWindow());
        if (root != null && root.exists() && root.isDirectory())
        {
            this.fireEvent(new MenuBarEvent(MenuBarEvent.OPEN_FOLDER, root));
            LocalUser.settings.getLastModifiedProject().setProjectRoot(root);
        }
    }
}
