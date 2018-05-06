package io.orbit.api.highlighting;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:55
 */
public class RegexSyntaxHighlighter implements SyntaxHighlighter
{
    private final RegexStylePattern regexPattern;

    public RegexSyntaxHighlighter(RegexStylePattern pattern)
    {
        this.regexPattern = pattern;
    }

    @Override
    public StyleSpans<Collection<String>> computeHighlighting(String text)
    {
        if (text == null || text.isEmpty() || text.matches("^\\s+$"))
            return null;

        int lastEnd = 0;
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        Matcher matcher = regexPattern.pattern.matcher(text);
        while (matcher.find())
        {
            for (Map.Entry<String, HighlightType> set : regexPattern.groupStyleMap.entrySet())
            {
                String styleClass = matcher.group(set.getKey()) == null ? null : set.getValue().className;
                if (styleClass != null)
                {
                    builder.add(Collections.emptyList(), matcher.start(set.getKey()) - lastEnd);
                    builder.add(Collections.singleton(styleClass), matcher.end(set.getKey()) - matcher.start(set.getKey()));
                    lastEnd = matcher.end(set.getKey());
                }
            }
        }
        return builder.create();
    }
    @Override
    public Duration getHighlightingInterval()
    {
        return Duration.ofMillis(50);
    }
}
