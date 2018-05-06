package io.orbit.api.highlighting;

import org.fxmisc.richtext.model.StyleSpans;
import java.time.Duration;
import java.util.Collection;

/**
 * Created by Tyler Swann on Friday April 27, 2018 at 15:24
 */
public interface SyntaxHighlighter
{
    StyleSpans<Collection<String>> computeHighlighting(String text);
    default Duration getHighlightingInterval()
    {
        return Duration.ofMillis(500);
    }
}
