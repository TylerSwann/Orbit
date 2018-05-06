package io.orbit.api.text;

import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyledDocument;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday February 18, 2018 at 15:29
 */
public class IndexedDocument
{
    public final IndexedLine[] indexedLines;
    public final int totalCharacterCount;
    public final List<IndexedLine> lines;

    public IndexedDocument(StyledDocument<Collection<String>, String, Collection<String>> document)
    {
        int totalCharCount = 0;
        this.indexedLines = new IndexedLine[document.getParagraphs().size()];
        for (int i = 0; i < this.indexedLines.length; i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> line = document.getParagraph(i);
            int startChar = totalCharCount;
            totalCharCount += line.length() <= 0 ? 1 : line.length();
            this.indexedLines[i] = new IndexedLine(startChar, totalCharCount, line.length(), i, line.getText());
            if (line.length() > 0)
                totalCharCount++;
        }
        this.lines = Arrays.asList(this.indexedLines);
        this.totalCharacterCount = totalCharCount;
    }
}
