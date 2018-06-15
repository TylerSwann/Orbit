package io.orbit.webtools.formatting;

import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyledDocument;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 14:53
 */
public class PatternIndentationMap implements IndentationMap
{
    public final PatternPair pattern;
    private final Map<Integer, Integer> indentMap = new HashMap<>();

    public PatternIndentationMap(String leftRegex, String rightRegex)
    {
        this.pattern = new PatternPair(leftRegex, rightRegex);
    }

    public PatternIndentationMap(Pattern left, Pattern right)
    {
        this.pattern = new PatternPair(left, right);
    }

    public PatternIndentationMap(PatternPair pattern)
    {
        this.pattern = pattern;
    }



    public void compute(StyledDocument<Collection<String>, String, Collection<String>> document)
    {
        int indentLevel = 0;
        this.indentMap.clear();
        final int size = document.getParagraphs().size();
        for (int i = 0; i < size; i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> paragraph = document.getParagraph(i);
            Matcher leftMatcher = this.pattern.left.matcher(paragraph.getText());
            Matcher rightMatcher = this.pattern.right.matcher(paragraph.getText());
            while (leftMatcher.find())
                indentLevel++;
            while (rightMatcher.find())
                indentLevel--;
            this.indentMap.put(i, indentLevel);
        }
    }

    public int indentLevelForLine(int lineNumber)
    {
        if (!this.indentMap.keySet().contains(lineNumber))
            return -1;
        return this.indentMap.get(lineNumber);
    }
}
