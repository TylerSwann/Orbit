package io.orbit.util;

import javafx.scene.control.IndexRange;

/**
 * Created by Tyler Swann on Saturday August 25, 2018 at 14:13
 */
public class Strings
{
    private Strings() {}

    public static IndexRange alphanumericIndex(String text)
    {
        int start = -1;
        int end = -1;
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++)
        {
            char character = characters[i];
            if (character != ' ' && start == -1)
                start = i;
            if (character != ' ')
                end = i;
        }
        return new IndexRange(start, end);
    }
    public static String trailingSpace(String text)
    {
        IndexRange range = alphanumericIndex(text);
        return text.substring(0, range.getStart());
    }

    public static String removeTrailingSpace(String text)
    {
        IndexRange range = alphanumericIndex(text);
        return text.substring(range.getStart(), range.getEnd());
    }
}
