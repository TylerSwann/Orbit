package io.orbit.webtools.formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Friday June 22, 2018 at 16:36
 */
public class HTMLCodeFormatter
{
    private static final Pattern VOID_TAG_PATTERN = Pattern.compile("(?<VOIDTAG><(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr)[\\sa-zA-Z0-9=\\\".=+\\-_:?/,]*>)");
    private static final Pattern OPEN_TAG_PATTERN = Pattern.compile("(?<OPENTAG>((<)(?!(area|base|br|col|command|embed|hr|img|input|keygen|link|menuitem|meta|param|source|track|wbr))[a-zA-Z0-9]+[\\sa-zA-Z0-9=\\\":/.-]*>))");
    private static final Pattern CLOSE_TAG_PATTERN = Pattern.compile("(?<CLOSETAG>(</[a-zA-Z0-9\\-]+>))");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(?<COMMENT>(<!--.*-->))");
    public static Map<Integer, String> indentMap = new HashMap<>();

    public HTMLCodeFormatter()
    {

    }

    public static String format(String source)
    {
        indentMap.clear();
        int indentLevel = -1;
        source = trimmed(source);
        StringBuilder builder = new StringBuilder("");
        for (String line : source.split("\n"))
        {
            if (line.matches("^[\\s\n]+"))
            {
                builder.append(String.format("%s\n", getIndent(indentLevel)));
                continue;
            }
            Matcher openMatcher = OPEN_TAG_PATTERN.matcher(line);
            Matcher closeMatcher = CLOSE_TAG_PATTERN.matcher(line);
            Matcher voidMatcher = VOID_TAG_PATTERN.matcher(line);
            Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
            if (commentMatcher.matches())
            {
                builder.append(String.format("%s%s\n", getIndent(indentLevel + 1), line));
                continue;
            }
            while (openMatcher.find())
                indentLevel++;
            if (voidMatcher.matches())
                builder.append(String.format("%s%s\n", getIndent(indentLevel + 1), line));
            else
                builder.append(String.format("%s%s\n", getIndent(indentLevel), line));
            while (closeMatcher.find())
                indentLevel--;
        }
        return builder.toString();
    }

    public static void computeIndentation(String source)
    {
        Map<Integer, String> map = new HashMap<>();
        Matcher openMatcher = OPEN_TAG_PATTERN.matcher(source);
        Matcher closeMatcher = CLOSE_TAG_PATTERN.matcher(source);
        Matcher voidMatcher = VOID_TAG_PATTERN.matcher(source);
        while (openMatcher.find())
        {
            int start = openMatcher.start();
            int end = openMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("OPEN: %d -> %d\n\t%s", start, end, text));
        }
        while (closeMatcher.find())
        {
            int start = closeMatcher.start();
            int end = closeMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("CLOSE: %d -> %d\n\t%s", start, end, text));
        }
        while (voidMatcher.find())
        {
            int start = voidMatcher.start();
            int end = voidMatcher.end();
            String text = source.substring(start, end).replaceAll("\n", "");
            System.out.println(String.format("VOID: %d -> %d\n\t%s", start, end, text));
        }
    }

    private static String getIndent(int level)
    {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < level; i++)
            builder.append("    ");
        return builder.toString();
    }
    private static String trimmed(String source)
    {
        StringBuilder builder = new StringBuilder();
        for (String line : source.split("\n"))
        {
            String trimmed = line.replaceAll("^\\s+", "");
            builder.append(String.format("%s\n", trimmed));
        }
        return builder.toString();
    }
}
