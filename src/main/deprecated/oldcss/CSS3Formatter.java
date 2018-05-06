package io.orbit.text.webtools;

import io.orbit.api.CharacterPair;
import io.orbit.api.CodeFormatter;
import io.orbit.api.Indentation;
import io.orbit.api.NotNullable;
import io.orbit.text.OrbitEditor;
import io.orbit.api.text.IndexedDocument;
import io.orbit.text.IndentationMap;
import io.orbit.api.text.IndexedLine;

/**
 * Created by Tyler Swann on Sunday March 25, 2018 at 14:56
 */
@Deprecated
public class CSS3Formatter implements CodeFormatter
{
    private IndentationMap formatMap;
    private OrbitEditor editor;

    @Override
    public void start(OrbitEditor editor)
    {
        this.editor = editor;
        this.formatMap = new IndentationMap(new CharacterPair('{', '}'));
        this.formatMap.compute(this.editor.getDocument());

    }



    @NotNullable
    @Override
    public Indentation indentationForLine(int lineNumber)
    {
        this.formatMap.compute(this.editor.getDocument());
        IndexedLine line = new IndexedDocument(this.editor.getDocument()).indexedLines[lineNumber];
        int lineStart = line.start;
        int indentLevel = this.formatMap.indentLevelForLine(line.number);
        String expectedIndentation = this.getIndentation(indentLevel);
        if (line.isEmpty && !line.isBlank)
        {
            int length = expectedIndentation.length();
            String currentIndentation = (lineStart - length) < 0 ? null : this.editor.getText(lineStart - length, lineStart);
            if (currentIndentation != null && !(this.editor.getText(lineStart - length, lineStart).equals(expectedIndentation)))
                return new Indentation(lineStart, indentLevel);
        }
        if (line.isBlank)
            return new Indentation(lineStart, indentLevel);
        return Indentation.EMPTY;
    }

    private String getIndentation(int level)
    {
        StringBuilder builder = new StringBuilder("");
        String indent = "    ";
        for (int i = 0; i < level; i++)
            builder.append(indent);
        return builder.toString();
    }

    @Override
    public Indentation indentationForNewLine(int lineNumber, int characterIndex)
    {
        IndexedLine currentLine = this.getCurrentLine();
        int indentLevel = this.formatMap.indentLevelForLine(currentLine.number);
        Indentation indentation;
        if (currentLine.end > this.editor.getDocument().length())
            indentation = new Indentation(currentLine.end - 1, indentLevel);
        else
            indentation = new Indentation(currentLine.end, indentLevel);
        if (currentLine.text.equals(""))
            this.editor.moveTo(this.editor.getCaretPosition() - 1);
        return indentation;
    }

    private IndexedDocument getDocument()
    {
        return new IndexedDocument(this.editor.getDocument());
    }
    private IndexedLine getCurrentLine()
    {
        return this.getDocument().indexedLines[this.editor.getFocusPosition().line];
    }
}
