package io.orbit.text.plaintext;

import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;
import io.orbit.api.highlighting.RegexSyntaxHighlighter;
import io.orbit.api.highlighting.SyntaxHighlighter;

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
    public String getFileNameExtension()
    {
        return "txt";
    }
}
