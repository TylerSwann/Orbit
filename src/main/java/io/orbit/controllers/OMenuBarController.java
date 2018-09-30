package io.orbit.controllers;

import io.orbit.App;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.notification.Notifications;
import io.orbit.api.notification.modal.MUIInputModal;
import io.orbit.api.notification.modal.MUIModal;
import io.orbit.api.notification.modal.MUIModalButton;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.text.FileType;
import io.orbit.controllers.events.MenuBarEvent;
import io.orbit.settings.*;
import io.orbit.ui.menubar.ApplicationMenuBar;
import io.orbit.ui.menubar.SystemMenuBar;
import io.orbit.util.OS;
import io.orbit.util.EventTargetObject;
import io.orbit.util.Tuple;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;

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
        this.addEventHandler(MenuBarEvent.FIND, event -> this.showFindAndReplaceDialog());
        this.addEventHandler(MenuBarEvent.SAVE, this::saveFile);
        this.addEventHandler(MenuBarEvent.SAVE_ALL, event -> this.saveAll());
        this.addEventHandler(MenuBarEvent.SETTINGS, event -> Platform.runLater(SettingsPage::show));
        this.addEventHandler(MenuBarEvent.NEW_PROJECT, event -> Platform.runLater(OProjectCreationDialog::show));
        this.addEventHandler(MenuBarEvent.OPEN_FILE, event -> this.showFileChooserDialog());
        this.addEventHandler(MenuBarEvent.OPEN_FOLDER, event -> this.showFolderChooseDialog());
        this.addEventHandler(MenuBarEvent.NEW_FILE, event -> showFileCreationDialog(false));
        this.addEventHandler(MenuBarEvent.NEW_FOLDER, event -> showFileCreationDialog(true));
        this.addEventHandler(MenuBarEvent.CUT, __ -> activeEditor.ifPresent(CodeEditor::cut));
        this.addEventHandler(MenuBarEvent.COPY, __ -> activeEditor.ifPresent(CodeEditor::copy));
        this.addEventHandler(MenuBarEvent.PASTE, __ -> activeEditor.ifPresent(CodeEditor::paste));
        this.addEventHandler(MenuBarEvent.NEW_CUSTOM_FILE_TYPE, event -> event.getFileType().ifPresent(this::showFileCreationDialog));
    }

    private void showFileCreationDialog(FileType fileType)
    {
        BiConsumer<ActionEvent, String> onCreate = (event, text) -> {
            String fileName = text == null ? "" : text;
            if (!fileName.equals("") && !fileName.matches("^[\\w _-]+\\.\\w+$"))
                fileName = String.format("%s.%s", fileName, fileType.getExtension());
            else
            {
                String[] pieces = fileName.split("\\.");
                String extension;
                if (pieces.length >= 2)
                {
                    extension = pieces[1];
                    if (!extension.equals(fileType.getExtension()))
                        fileName = String.format("%s.%s", fileName, fileType.getExtension());
                }
            }
            Tuple<Boolean, File> result = validateAndCreateFile(false, fileName);
            if (result.first)
                fileType.getOnCreate().accept(result.second);
        };

        showCreationDialog(String.format("New %s", fileType.getDisplayText()), "Name: ", onCreate);
    }

    private void showFileCreationDialog(boolean directory)
    {
        String type = directory ? "directory" : "file";
        BiConsumer<ActionEvent, String> onCreate = (event, text) -> {
            String fileName = text == null ? "" : text;
            validateAndCreateFile(directory, fileName);
        };
        showCreationDialog(String.format("Create %s", type), String.format("Enter a new %s name:", type), onCreate);
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

    private void showFindAndReplaceDialog()
    {
        CodeEditor editor = App.controller().getActiveEditor();
        editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.FIND, editor.getFile()));
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

    private Tuple<Boolean, File> validateAndCreateFile(boolean directory, String fileName)
    {
        String type = directory ? "directory" : "file";
        boolean hasError;
        final File file = new File(String.format("%s\\%s", LocalUser.project.getProjectRoot().getPath(), fileName));
        if (fileName.equals("")|| !fileName.matches("^[\\w\\-.]+$"))
        {
            Notifications.showErrorAlert("ERROR", String.format("Sorry, that is not a valid %s name.", type));
            hasError = true;
        }
        else if (file.exists())
        {
            Notifications.showErrorAlert("ERROR", String.format("Sorry, that %s already exists!", type));
            hasError = true;
        }
        else
        {

            try
            {
                if (!directory)
                    hasError = !file.createNewFile();
                else
                    hasError = !file.mkdir();
                App.controller().getProjectNavigatorController().forceRefresh();
            }
            catch (IOException ex)
            {
                hasError = true;
                ex.printStackTrace();
            }
            if (hasError)
                Notifications.showErrorAlert("ERROR", String.format("Sorry, we ran into a problem and were unable to create that %s.", type));
        }
        return new Tuple<>(hasError, file);
    }

    private void showCreationDialog(String title, String message, BiConsumer<ActionEvent, String> onCreate)
    {
        MUIModalButton cancel = new MUIModalButton("CANCEL", MUIModalButton.MUIModalButtonStyle.SECONDARY);
        MUIModalButton create = new MUIModalButton("CREATE", MUIModalButton.MUIModalButtonStyle.PRIMARY);
        MUIInputModal modal = new MUIInputModal(title, message, cancel, create);
        Notifications.showModal(modal);
        create.setOnAction(event -> onCreate.accept(event, modal.getText()));
    }
}
