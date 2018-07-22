package io.orbit.controllers;

import io.orbit.App;
import io.orbit.settings.LocalUser;
import io.orbit.settings.OrbitFile;
import io.orbit.settings.UnownedProjectFile;
import io.orbit.controllers.events.ApplicationEvent;
import io.orbit.api.event.DocumentEvent;
import io.orbit.ui.MUIDialog;
import io.orbit.util.Size;
import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Tyler Swann on Sunday April 15, 2018 at 16:33
 */
@Deprecated
public class AppEventsReceiver
{
    private static final DataFormat FILE_PATH = new DataFormat("file/path");
//    private OStatusBarController statusBarController;
    private Clipboard clipboard;
    private SettingsWindow settingsWindow;

    public AppEventsReceiver()
    {

        Platform.runLater(this::registerListeners);
    }
    private void registerListeners()
    {
//        LanguageService.open(OEditorController.activeEditorProperty(), 1);
//        OProjectTreeViewController projectTreeViewController = App.applicationController().getProjectTreeViewController();

//        this.statusBarController = App.applicationController().getStatusBarController();
//        projectTreeViewController.addEventHandler(IOEvent.CREATE_FILE, event -> event.getTargetFile().ifPresent(this::createNewFileEvent));
//        projectTreeViewController.addEventHandler(IOEvent.CREATE_DIRECTORY, event -> event.getTargetFile().ifPresent(this::createNewDirectoryEvent));
//        projectTreeViewController.addEventHandler(IOEvent.DELETE_FILE, event -> event.getTargetFile().ifPresent(this::deleteFileEvent));
//        projectTreeViewController.addEventHandler(IOEvent.CUT_FILE, event -> event.getTargetFile().ifPresent(this::cutFile));
//        projectTreeViewController.addEventHandler(IOEvent.COPY_FILE, event -> event.getTargetFile().ifPresent(this::copyFile));
//        projectTreeViewController.addEventHandler(IOEvent.COPY_PATH, event -> event.getTargetFile().ifPresent(this::copyFilePath));
//        projectTreeViewController.addEventHandler(IOEvent.COPY_RELATIVE_PATH, event -> event.getTargetFile().ifPresent(this::copyFileRelativePath));
//        projectTreeViewController.addEventHandler(IOEvent.PASTE_FILE, event -> event.getTargetFile().ifPresent(this::pasteFile));

        App.appEventsProperty.addEventListener(ApplicationEvent.WILL_CLOSE, event -> this.saveWindowSizeToSettings());
        // TODO - Editor controller events
//        OEditorController editorController = App.applicationController().getEditorController();
//        editorController.addEventHandler(DocumentEvent.SAVE_FILE, event -> event.getSourceFile().ifPresent(this::saveFile));
//        editorController.addEventHandler(DocumentEvent.SAVE_ALL, event -> this.saveAll());
//        editorController.addEventHandler(DocumentEvent.SAVE_NON_PROJECT_FILE, event -> event.getSourceFile().ifPresent(this::saveNonProjectFile));

    }

    private void saveNonProjectFile(OrbitFile file)
    {
        if (!(file instanceof UnownedProjectFile))
            throw new RuntimeException("Cannot save OrbitFile as UnownedProjectFile if OrbitFile is not instance of UnownedProjectFile!!!");
        UnownedProjectFile nonProjectFile = (UnownedProjectFile) file;
        nonProjectFile.save();
        switch (nonProjectFile.mode)
        {
            case EDIT_UI_THEME:
                App.setApplicationTheme(nonProjectFile);
                break;
            case EDIT_SYNTAX_THEME:
                App.setSyntaxTheme(nonProjectFile);
                break;
            default:
                break;
        }
    }

    private void showCreateDialog(String title, String message, String prompt, Consumer<Optional<String>> onConfirm)
    {
        MUIDialog dialog = new MUIDialog(title, "CANCEL", "CREATE", message, prompt);
        dialog.setOnPrimaryClick(onConfirm);
        dialog.show(App.PRIMARY_STAGE.get());
    }

    private void showDeleteDialog(String title, String message, Consumer<Optional<String>> onConfirm)
    {
        MUIDialog dialog = new MUIDialog(title, "CANCEL", "DELETE", message, true);
        dialog.setOnPrimaryClick(onConfirm);
        dialog.show(App.PRIMARY_STAGE.get());
    }

    private void showConfirmOverwriteDialog(String fileName, Runnable onConfirm)
    {
        MUIDialog dialog = new MUIDialog("Replace File",
                "Cancel",
                "OverWrite",
                String.format("The destination already has a file named %s. Would you like to over write it?", fileName));
        dialog.setOnPrimaryClick(unused -> onConfirm.run());
        dialog.show(App.PRIMARY_STAGE.get());
    }

    private void alert(String title, String message)
    {
        MUIDialog dialog = new MUIDialog(title, "Cancel", "Ok", message, true);
        dialog.show(App.PRIMARY_STAGE.get());
    }

    private void createNewFileEvent(File targetFile)
    {
        showCreateDialog("New File", "Enter the file name:", "Name...", optInput -> optInput.ifPresent(fileName -> {
            File parentDir = targetFile.isDirectory() ? targetFile : targetFile.getParentFile();
            File newFile = new File(String.format("%s\\%s", parentDir.getPath(), fileName));
            try { newFile.createNewFile(); }
            catch (IOException ex) {  ex.printStackTrace();  }
            App.applicationController().getProjectNavigatorController().forceRefresh();
        }));
    }

    private void createNewDirectoryEvent(File targetFile)
    {
        showCreateDialog("New Directory", "Enter the directory name:", "Name...", optInput -> optInput.ifPresent(fileName -> {
            File parentDir = targetFile.isDirectory() ? targetFile : targetFile.getParentFile();
            File newFile = new File(String.format("%s\\%s", parentDir.getPath(), fileName));
            newFile.mkdir();
            App.applicationController().getProjectNavigatorController().forceRefresh();
        }));
    }

    private void deleteFileEvent(File targetFile)
    {
        String message = String.format("Are you certain you want to delete %s? This cannot be undone.", targetFile.getName());
        this.showDeleteDialog("DELETE", message, unused -> {
            if (!targetFile.isDirectory())
                targetFile.delete();
            else
            {
                try
                {
                    FileUtils.deleteDirectory(targetFile);
                }
                catch (IOException e)
                {
                    alert("ERROR", "Could't perform directory delete operation.");
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> App.applicationController().getProjectNavigatorController().forceRefresh());
        });
    }

    private void cutFile(File file)
    {
        //TODO - change opacity of files that are cut in OProjectTreeViewController
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(FILE_PATH, file);
        content.put(DataFormat.PLAIN_TEXT, "cut");
        clipboard.setContent(content);
    }
    private void copyFile(File file)
    {
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(FILE_PATH, file);
        content.put(DataFormat.PLAIN_TEXT, "copy");
        clipboard.setContent(content);
    }
    private void copyFilePath(File file)
    {
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, file.getPath());
        clipboard.setContent(content);
    }
    private void copyFileRelativePath(File file)
    {
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        File root = LocalUser.userSettings.getLastModifiedProject().getProjectRoot();
        File relativeFile = new File(file.getPath().replace(root.getPath(), ""));
        String relativePath = relativeFile.getPath().replace("\\", "/");
        if (relativePath.length() > 0 && relativePath.toCharArray()[0] == '/')
            relativePath = relativePath.substring(1, relativePath.length() - 1);
        Map<DataFormat, Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, relativePath);
        clipboard.setContent(content);
    }
    private void pasteFile(File pasteLocationFile)
    {
        if (clipboard == null)
            clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasContent(FILE_PATH) && clipboard.hasString())
        {
            File source = (File) clipboard.getContent(FILE_PATH);
            String cutOrCopy = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
            File destinationFolder = pasteLocationFile.isDirectory() ? pasteLocationFile : pasteLocationFile.getParentFile();
            File destination = new File(String.format("%s\\%s", destinationFolder.getPath(), source.getName()));
            Runnable performOperation = () -> {
                try
                {
                    if (source.isDirectory())
                    {
                        if (cutOrCopy.equals("cut"))
                            FileUtils.moveDirectory(source, destination);
                        else if (cutOrCopy.equals("copy"))
                            FileUtils.copyDirectory(source, destination);
                    }
                    else
                        FileUtils.moveFile(source, destination);
                }
                catch (IOException ex)
                {
                    alert("ERROR", "Couldn't perform files operation");
                    ex.printStackTrace();
                }
                Platform.runLater(() -> App.applicationController().getProjectNavigatorController().forceRefresh());
            };
            if (destination.exists())
                showConfirmOverwriteDialog(destination.getName(), performOperation);
            else
                performOperation.run();
        }
    }

    private void saveFile(File file, String source)
    {
        try
        {
            byte[] data = source.getBytes(Charset.defaultCharset());
            Files.write(Paths.get(file.getPath()), data);
//            statusBarController.showSnackBarMessage(String.format("Saved %s", file.getName()), 2000);
        }
        catch (IOException ex)
        {
            System.out.println("ERROR saving file");
            alert("ERROR", String.format("Couldn't save file at path %s", file.getPath()));
            ex.printStackTrace();
        }
    }

    private void saveFile(OrbitFile file)
    {
        file.save();
//        this.statusBarController.showSnackBarMessage(String.format("Saved %s!", file.getName()), 2000);
    }

    private void saveAll()
    {
//        App.applicationController().getEditorController().getOpenProjectFiles().forEach(file -> {
//            if (file.wasModified())
//                file.save();
//        });
//        this.statusBarController.showSnackBarMessage("Saved All!", 500);
    }

    private void saveWindowSizeToSettings()
    {
        Stage stage = App.PRIMARY_STAGE.get();
        Size windowSize = new Size(stage.getWidth(), stage.getHeight());
        LocalUser.userSettings.setWindowSize(windowSize);
    }
}
