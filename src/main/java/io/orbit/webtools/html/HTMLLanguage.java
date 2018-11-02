package io.orbit.webtools.html;

import io.orbit.api.language.LanguageDelegate;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import io.orbit.webtools.WebToolsController;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 17:24
 */
public class HTMLLanguage implements LanguageDelegate
{
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new RegexSyntaxHighlighter(new HTMLRegexPattern());
    }

    @Override
    public FileType getFileNameExtension()
    {
        return WebToolsController.HTML_FILE();
    }
}
