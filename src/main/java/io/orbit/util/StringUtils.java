package io.orbit.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Tyler Swann on Friday March 16, 2018 at 13:24
 */
public class StringUtils
{
    private StringUtils() { }

    public static IndexRange firstDifference(CharSequence one, CharSequence two)
    {
        int start = -1;
        int end = -1;
        //int length = one.length() < two.length() ? two.length() : one.length();
        int minLength = Math.min(one.length(), two.length());
        int maxLength = Math.max(one.length(), two.length());
        for (int i = 0; i < maxLength; i++)
        {
            if (i < minLength)
            {
                char first = one.charAt(i);
                char second = two.charAt(i);
                if (first != second && start == -1)
                    start = i;
                else if (first != second)
                    end = i;
            }
            else
                end = i;
        }
        return new IndexRange(start, end);
    }
    public static IndexRange[] differences(CharSequence one, CharSequence two)
    {
        int start = -1;
        int end = -1;
        ArrayList<IndexRange> ranges = new ArrayList<>();
        int minLength = Math.min(one.length(), two.length());
        int maxLength = Math.max(one.length(), two.length());
        for (int i = 0; i < maxLength; i++)
        {
            if (i < minLength)
            {
                char first = one.charAt(i);
                char second = two.charAt(i);
                if (first != second && start == -1)
                    start = i;
                else if (first != second)
                    end = i;
                else if (start != -1 && end != -1)
                {
                    ranges.add(new IndexRange(start, end));
                    start = -1;
                    end = -1;
                }
            }
            else if (start == -1)
                start = i;
            else
                end = i;
        }
        if (start != -1 && end != -1)
            ranges.add(new IndexRange(start, end));
        return ranges.toArray(new IndexRange[ranges.size()]);
    }

    public static void stringify(File input, File output)
    {
        try
        {
            String inputString = new String(Files.readAllBytes(Paths.get(input.getPath())));

            String[] stringified = stringify(inputString);
            PrintWriter writer = new PrintWriter(output.getPath());
            for (String string : stringified)
                writer.println(string);
            writer.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }
    public static String[] stringify(String input)
    {
        String[] pieces = input.split("\n");
        String[] stringified = new String[pieces.length];
        for (int i = 0; i < pieces.length; i++)
            stringified[i] = String.format("\"%s\",", pieces[i]);
        return stringified;
    }
}
