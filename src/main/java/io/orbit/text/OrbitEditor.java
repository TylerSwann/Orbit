package io.orbit.text;

import io.orbit.settings.OrbitFile;
import io.orbit.settings.ProjectFile;
import io.orbit.settings.UnownedProjectFile;
import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.event.DocumentEvent;
import io.orbit.plugin.PluginDispatch;
import io.orbit.settings.UserHotKeys;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 14:31
 */
public class OrbitEditor extends CodeEditor
{
    public final OrbitFile file;

    private LanguageDelegate language;
    private List<EditorController> activeControllers = new ArrayList<>();
    private boolean hasUnsavedChanges = false;

    public OrbitEditor(OrbitFile file)
    {
        super(file);
        this.file = file;
        registerPlugins();
        registerEvents();
    }

    private void registerEvents()
    {
        Platform.runLater(() -> this.plainTextChanges().addObserver(event -> {
            if (!hasUnsavedChanges)
            {
                this.fireEvent(new CodeEditorEvent(CodeEditorEvent.FILE_WAS_MODIFIED, this.file));
                this.hasUnsavedChanges = true;
            }
        }));
        addHotKeyEvents();
    }

    private void addHotKeyEvents()
    {
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (UserHotKeys.SAVE_COMBO().match(event))
            {
                if (this.file instanceof ProjectFile)
                    this.fireEvent(new DocumentEvent(DocumentEvent.SAVE_FILE, this.file));
                else if (this.file instanceof UnownedProjectFile)
                    this.fireEvent(new DocumentEvent(DocumentEvent.SAVE_NON_PROJECT_FILE, this.file ));
                this.hasUnsavedChanges = false;
            }
            else if (UserHotKeys.SAVE_ALL_COMBO().match(event))
                this.fireEvent(new DocumentEvent(this, this, DocumentEvent.SAVE_ALL));
            else if (UserHotKeys.FIND_COMBO().match(event))
                this.fireEvent(new DocumentEvent(this, this, DocumentEvent.FIND));
            else if (UserHotKeys.FIND_REPLACE_COMBO().match(event))
                this.fireEvent(new DocumentEvent(this, this, DocumentEvent.FIND_AND_REPLACE));
        });
    }

    private void registerPlugins()
    {
        String fileType = extensionOfFile(this.file);
        List<PluginController> plugins = PluginDispatch.controllersForFileType(fileType);
        for (PluginController plugin : plugins)
        {
            EditorController controller = plugin.getEditorController(this.file, fileType);
            if (this.language == null)
            {
                LanguageDelegate language = plugin.getLanguageDelegate(this.file, fileType);
                if (language != null)
                    this.language = language;
            }
            if (controller != null)
                this.activeControllers.add(controller);
        }
        this.activeControllers.forEach(controller -> Platform.runLater(() -> controller.start(this.file, this)));
    }

    private String extensionOfFile(File file)
    {
        int index = file.getName().lastIndexOf('.');
        String extension = null;
        if (index > 0)
            extension = file.getName().substring(index + 1);
        return extension;
    }

    public boolean hasUnsavedChanges() {  return this.hasUnsavedChanges;  }
    public LanguageDelegate getLanguage() { return language; }
}
