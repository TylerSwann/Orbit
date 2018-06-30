package io.orbit.text.plaintext;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.SVGIcon;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.api.text.FileType;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday June 02, 2018 at 15:23
 */
public class PlainText implements LanguageDelegate
{
    @Override
    public SyntaxHighlighter getSyntaxHighlighter()
    {
        Map<String, HighlightType> highlightMap = new HashMap<>();
        highlightMap.put("empty", HighlightType.EMPTY);
        return new RegexSyntaxHighlighter(new RegexStylePattern(Pattern.compile("(?<EMPTY>.)"), highlightMap));
    }

    @Override
    public FileType getFileNameExtension()
    {
        return new FileType("txt", "text", new SVGIcon(FontAwesomeSolid.FILE_ALT));
    }
}
