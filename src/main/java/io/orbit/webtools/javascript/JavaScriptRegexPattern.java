package io.orbit.webtools.javascript;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaScriptRegexPattern extends RegexStylePattern
{
    private static final Pattern pattern;
    private static final Map<String, HighlightType> map;

    private static final String KEYWORDS = "";

    static {
        pattern = Pattern.compile("");
        map = new HashMap<>();
    }

    public JavaScriptRegexPattern()
    {
        super(pattern, map);
    }
}
