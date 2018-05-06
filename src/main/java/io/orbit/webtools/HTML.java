package io.orbit.webtools;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:24
 */
public class HTML implements LanguageDelegate
{
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new RegexSyntaxHighlighter(HTMLRegexPattern.STYLE_PATTERN);
    }

    @Override
    public String getFileNameExtension()
    {
        return "html";
    }
}
