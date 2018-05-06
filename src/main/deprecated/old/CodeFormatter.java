package io.orbit.api;

import io.orbit.api.text.CodeEditor;

/**
 * Created by Tyler Swann on Sunday March 25, 2018 at 14:48
 */
@Deprecated
public interface CodeFormatter
{
    void start(CodeEditor editor);
    @NotNullable
    Indentation indentationForLine(int line);
    @NotNullable
    Indentation indentationForNewLine(int lineNumber, int characterIndex);
}
