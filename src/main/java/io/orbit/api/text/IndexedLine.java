package io.orbit.api.text;

/**
 * Created by Tyler Swann on Sunday February 18, 2018 at 15:29
 */
public class IndexedLine
{
    public final int start;
    public final int end;
    public final int length;
    public final int number;
    public final boolean isEmpty;
    public final boolean isBlank;
    public final String text;

    IndexedLine(int characterIndexAtStartOfLine, int characterIndexAtEndOfLine, int length, int number, String text)
    {
        this.start = characterIndexAtStartOfLine;
        this.end = characterIndexAtEndOfLine;
        this.length = length;
        this.number = number;
        this.isEmpty = text.isEmpty();
        this.isBlank = text.matches("^\\s+$");
        this.text = text;
    }
}
