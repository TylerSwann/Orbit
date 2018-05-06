package io.orbit.api.formatting;

import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Saturday April 14, 2018 at 15:18
 */
public class PatternPair
{
    public final Pattern left;
    public final Pattern right;

    public PatternPair(String leftRegex, String rightRegex)
    {
        this.left = Pattern.compile(leftRegex);
        this.right = Pattern.compile(rightRegex);
    }

    public PatternPair(Pattern left, Pattern right)
    {
        this.left = left;
        this.right = right;
    }
}
