package io.orbit.webtools;

import io.orbit.api.text.CodeEditor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by Tyler Swann on Saturday June 23, 2018 at 19:04
 */
public abstract class CodeFormatter
{
    public abstract String reformat(String source);
    protected final CodeEditor editor;
    protected SimpleObjectProperty<Boolean> paused = new SimpleObjectProperty<>(true);
    public void pause() { this.paused.set(true); }
    public void play() {  this.paused.set(false);  }
    public ObservableValue<Boolean> pausedProperty() {  return paused;  }
    public boolean isPaused() {  return this.paused.get();  }

    public CodeFormatter(CodeEditor editor)
    {
        this.editor = editor;
    }
    private CodeFormatter()
    {
        this.editor = null;
    }

    protected String getIndent(int level)
    {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < level; i++)
            builder.append("    ");
        return builder.toString();
    }
}
