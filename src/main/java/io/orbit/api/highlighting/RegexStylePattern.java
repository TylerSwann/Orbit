package io.orbit.api.highlighting;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:02
 */
public class RegexStylePattern
{
    public final Pattern pattern;
    public final Map<String, HighlightType> groupStyleMap;

    public RegexStylePattern(Pattern pattern, Map<String, HighlightType> groupStyleMap)
    {
        this.pattern = pattern;
        this.groupStyleMap = groupStyleMap;
    }
    private RegexStylePattern()
    {
        this.pattern = null;
        this.groupStyleMap = null;
    }
}
