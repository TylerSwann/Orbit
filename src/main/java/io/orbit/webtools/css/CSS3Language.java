package io.orbit.webtools.css;

import io.orbit.api.*;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * Created by Tyler Swann on Friday February 16, 2018 at 14:00
 */
public class CSS3Language implements LanguageDelegate
{
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        return new RegexSyntaxHighlighter(new CSS3RegexPattern());
    }

    @Override
    public FileType getFileNameExtension()
    {
        return new FileType("css", "text/css", new SVGIcon(FontAwesomeBrands.CSS3_ALT));
    }
}