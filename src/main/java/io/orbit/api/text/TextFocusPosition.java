package io.orbit.api.text;

/**
 * Created by TylersDesktop on 12/29/2017.
 *
 */
public class TextFocusPosition
{
    public final int line;
    public final int caretPositionInDocument;
    public final int caretPositionInLine;


    public TextFocusPosition(int line, int caretPositionInLine, int caretPositionInDocument)
    {
        this.line = line;
        this.caretPositionInDocument = caretPositionInDocument;
        this.caretPositionInLine = caretPositionInLine;
    }
}