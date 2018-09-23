package io.orbit.controllers;

import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.notification.Notifications;
import io.orbit.api.text.CodeEditor;
import io.orbit.plugin.PluginDispatch;
import io.orbit.settings.HotKeys;
import io.orbit.settings.LocalUser;
import io.orbit.ui.contextmenu.EditorContextMenu;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Friday July 27, 2018 at 19:22
 */
public class OCodeEditorController
{
    private CodeEditor editor;
    private LanguageDelegate language;
    private List<EditorController> activeControllers = new ArrayList<>();
    private EditorContextMenu contextMenu;

    public OCodeEditorController(CodeEditor editor)
    {
        this.contextMenu = new EditorContextMenu(editor);
        this.editor = editor;
        registerPlugins();
        addHotKeyEvents();
        registerListeners();
    }

    private void registerListeners()
    {
        this.editor.addEventHandler(CodeEditorEvent.SAVE, __ -> this.save());
        this.editor.addEventHandler(CodeEditorEvent.SAVE_ALL, __ -> this.saveAll());
        this.contextMenu.addEventHandler(CodeEditorEvent.UNDO, __ -> this.editor.undo());
        this.contextMenu.addEventHandler(CodeEditorEvent.REDO, __ -> this.editor.redo());
        this.contextMenu.addEventHandler(CodeEditorEvent.CUT, __ -> this.editor.cut());
        this.contextMenu.addEventHandler(CodeEditorEvent.COPY, __ -> this.editor.copy());
        this.contextMenu.addEventHandler(CodeEditorEvent.PASTE, __ -> this.editor.paste());
        this.contextMenu.addEventHandler(CodeEditorEvent.SELECT_ALL, __ -> this.editor.selectAll());
    }

    private void addHotKeyEvents()
    {
        HotKeys hotKeys = LocalUser.userSettings.getHotKeys();
        this.editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (hotKeys.getCut().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.CUT, this.editor.getFile()));
            else if (hotKeys.getCopy().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.COPY, this.editor.getFile()));
            else if (hotKeys.getPaste().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.PASTE, this.editor.getFile()));
            else if (hotKeys.getUndo().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.UNDO, this.editor.getFile()));
            else if (hotKeys.getRedo().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.REDO, this.editor.getFile()));
            else if (hotKeys.getSave().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.SAVE, this.editor.getFile()));
            else if (hotKeys.getSaveAll().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.SAVE_ALL, this.editor.getFile()));
            else if (hotKeys.getFind().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.FIND, this.editor.getFile()));
            else if (hotKeys.getFindReplace().match(event))
                this.editor.fireEvent(new CodeEditorEvent(CodeEditorEvent.FIND_AND_REPLACE, this.editor.getFile()));
            else if (hotKeys.getFindInProject().match(event))
                System.out.println("TODO");
            else if (hotKeys.getFindReplaceInProject().match(event))
                System.out.println("TODO");
        });
    }

    private void save()
    {
        boolean success = trySaveEditor(this.editor);
        if (success)
            Notifications.showSnackBarMessage(String.format("Saved %s", this.editor.getFile().getName()));
        else
            Notifications.showSnackBarMessage("ERROR: Sorry, we were unable to save that file");
    }

    private void saveAll()
    {
        boolean allSuccess = true;
        for (OCodeEditorController controller : OEditorTabPaneController.ACTIVE_CONTROLLERS)
        {
            boolean success = trySaveEditor(controller.getEditor());
            if (!success)
                allSuccess = false;
        }
        if (allSuccess)
            Notifications.showSnackBarMessage("Saved All!");
        else
            Notifications.showSnackBarMessage("ERROR: Sorry, there was a problem saving some files");
    }

    private boolean trySaveEditor(CodeEditor editor)
    {
        try
        {
            byte[] data = editor.getText().getBytes();
            Files.write(Paths.get(editor.getFile().getPath()), data);
            return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    private void registerPlugins()
    {
        String fileType = extensionOfFile(this.editor.getFile());
        List<PluginController> plugins = PluginDispatch.controllersForFileType(fileType);
        for (PluginController plugin : plugins)
        {
            EditorController controller = plugin.getEditorController(this.editor.getFile(), fileType);
            if (this.language == null)
            {
                LanguageDelegate language = plugin.getLanguageDelegate(this.editor.getFile(), fileType);
                if (language != null)
                    this.language = language;
            }
            if (controller != null)
                this.activeControllers.add(controller);
        }
        this.activeControllers.forEach(controller -> Platform.runLater(() -> controller.start(this.editor.getFile(), this.editor)));
    }

    private String extensionOfFile(File file)
    {
        int index = file.getName().lastIndexOf('.');
        String extension = null;
        if (index > 0)
            extension = file.getName().substring(index + 1);
        return extension;
    }

    public LanguageDelegate getLanguage() { return language; }
    public CodeEditor getEditor() { return editor; }
}
