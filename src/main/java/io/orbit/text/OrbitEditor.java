package io.orbit.text;

import io.orbit.OrbitFile;
import io.orbit.ProjectFile;
import io.orbit.UnownedProjectFile;
import io.orbit.api.EditorController;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.PluginController;
import io.orbit.api.event.CodeEditorEvent;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.CodeEditor;
import io.orbit.api.event.DocumentEvent;
import io.orbit.plugin.PluginDispatch;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 14:31
 */
public class OrbitEditor extends CodeEditor
{
    public final OrbitFile file;

    private LanguageDelegate language;
    private List<EditorController> activeControllers = new ArrayList<>();
    private boolean hasUnsavedChanges = false;
    private ExecutorService highlightingService;
    private ObjectProperty<Boolean> highlightOnChange = new SimpleObjectProperty<>(true);

    public OrbitEditor(OrbitFile file)
    {
        super(file);
        this.file = file;
        registerPlugins();
        registerEvents();
    }

    private void registerEvents()
    {
        this.plainTextChanges().addObserver(event -> {
            if (!hasUnsavedChanges)
            {
                this.fireEvent(new CodeEditorEvent(CodeEditorEvent.FILE_WAS_MODIFIED, this.file));
                this.hasUnsavedChanges = true;
            }
        });
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.S && event.isControlDown())
            {
                if (this.file instanceof ProjectFile)
                    this.fireEvent(new DocumentEvent(this.file, this.textProperty().getValue(), this, this, DocumentEvent.SAVE_FILE));
                else if (this.file instanceof UnownedProjectFile)
                    this.fireEvent(new DocumentEvent(this.file, this.textProperty().getValue(), this, this, DocumentEvent.SAVE_NON_PROJECT_FILE));
                this.hasUnsavedChanges = false;
            }
        });
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F && event.isControlDown())
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
        if (this.language != null)
            this.addHighlightingTask();
        this.activeControllers.forEach(controller -> Platform.runLater(() -> controller.start(this.file, this)));
    }

    public void pauseHighlighting()
    {
        this.highlightOnChange.setValue(false);
    }

    private void addHighlightingTask()
    {
        this.highlightingService = Executors.newSingleThreadExecutor();
        SyntaxHighlighter highlighter = this.language.getSyntaxHighlighter();
        EventStream<PlainTextChange> changes = this.plainTextChanges();
        changes.successionEnds(highlighter.getHighlightingInterval())
                .conditionOn(this.highlightOnChange)
                .supplyTask(this::highlightAsynchronously)
                .awaitLatest(changes)
                .filterMap(attempt -> {
                    if (attempt == null || attempt.isFailure())
                        return Optional.empty();
                    return Optional.of(attempt.get());
                })
                .subscribe(this::applyHighlighting);
    }

    public void forceReHighlighting()
    {
        this.highlightOnChange.setValue(true);
        Task<StyleSpans<Collection<String>>> highlight = this.highlightAsynchronously();
        highlight.setOnSucceeded(event -> this.applyHighlighting(highlight.getValue()));
    }

    private Task<StyleSpans<Collection<String>>> highlightAsynchronously()
    {
        String text = this.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>()
        {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception
            {
                return language.getSyntaxHighlighter().computeHighlighting(text);
            }
        };
        this.highlightingService.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting)
    {
        if (highlighting.getSpanCount() > 0)
            this.setStyleSpans(0, highlighting);
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
}
