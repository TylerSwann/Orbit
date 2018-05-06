package io.orbit.webtools;

import io.orbit.api.*;
import io.orbit.api.autocompletion.AutoCompletionProvider;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;

/**
 * Created by Tyler Swann on Friday February 16, 2018 at 14:00
 */
public class CSS3 implements LanguageDelegate
{
    private static AutoCompletionProvider autoCompletionProvider;


    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new RegexSyntaxHighlighter(CSS3RegexPattern.STYLE_PATTERN);
    }

    @Override
    public String getFileNameExtension()
    {
        return "css";
    }

    @Override
    public AutoCompletionProvider getAutoCompletionProvider()
    {
        if (autoCompletionProvider == null)
            autoCompletionProvider = new CSS3AutoCompletionProvider();
        return autoCompletionProvider;
    }

}