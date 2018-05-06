package io.orbit.api;

import io.orbit.text.CodeEditor;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 13:29
 */
public abstract class CodeFormatter
{
    protected LanguageDelegate language;
    protected CodeEditor editor;

    public abstract Indentation indentationForLine(int line);

    public void setLanguage(LanguageDelegate language)
    {
        this.language = language;
    }

    public void setEditor(CodeEditor editor)
    {
        this.editor = editor;
    }

    public void format() throws UnsupportedOperationException
    {
        if (this.editor == null || this.language == null)
            throw new UnsupportedOperationException("CodeEditor and LanguageDelegate must not be null for CodeFormatter to format!");
    }
}