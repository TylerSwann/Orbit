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
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new RegexSyntaxHighlighter(new CSS3RegexPattern());
    }

    @Override
    public String getFileNameExtension()
    {
        return "css";
    }
}