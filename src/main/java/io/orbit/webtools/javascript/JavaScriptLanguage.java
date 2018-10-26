package io.orbit.webtools.javascript;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import io.orbit.webtools.WebToolsController;
import io.orbit.webtools.html.HTMLRegexPattern;

public class JavaScriptLanguage implements LanguageDelegate
{
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new JavaScriptHighlighter();
    }

    @Override
    public FileType getFileNameExtension()
    {
        return WebToolsController.JS_FILE();
    }
}
