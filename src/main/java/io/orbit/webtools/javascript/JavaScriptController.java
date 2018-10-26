package io.orbit.webtools.javascript;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;

import java.io.File;

public class JavaScriptController implements EditorController
{
    private JavaScriptCodeFormatter formatter;

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.formatter = new JavaScriptCodeFormatter(editor);
        this.registerListeners();
    }

    private void registerListeners()
    {
        this.formatter.play();
    }
}
