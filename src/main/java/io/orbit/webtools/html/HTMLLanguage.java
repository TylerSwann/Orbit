package io.orbit.webtools.html;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.SVGIcon;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

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
        return new FileType("html", "text/html", new SVGIcon(FontAwesomeBrands.HTML5));
    }
}
