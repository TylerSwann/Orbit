package io.orbit.text.webtools;

import io.orbit.text.antlr.html.HTMLLexer;
import io.orbit.text.antlr.html.HTMLParser;
import io.orbit.text.antlr.html.HTMLParserBaseListener;
import io.orbit.text.formatting.IndentationMap;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyledDocument;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Saturday April 21, 2018 at 12:38
 */
public class HTMLIndentationMap extends HTMLParserBaseListener implements IndentationMap
{
    private static final HTMLLexer lexer;
    private static final HTMLParser parser;
    private StyledDocument<Collection<String>, String, Collection<String>> document;

    static {
        lexer = new HTMLLexer(CharStreams.fromString(""));
        parser = new HTMLParser(new CommonTokenStream(lexer));
    }

    private int indentLevel = -1;
    private int lastLine = 0;
    private Map<Integer, Integer> indentMap = new HashMap<>();
    private final ParseTreeWalker walker = new ParseTreeWalker();

    @Override
    public void enterElement(HTMLParser.ElementContext ctx)
    {
        int lineStart = ctx.start.getLine() - 1;
        int lineEnd = ctx.stop.getLine() - 1;
        if (lineStart == lineEnd)
        {
            this.indentMap.put(lineStart, this.indentLevel + 1);
            return;
        }
        this.indentLevel++;
        for (int i = lineStart; i <= lineEnd; i++)
        {
            if (this.indentMap.containsKey(i))
                this.indentMap.remove(i);

            this.indentMap.put(i, this.indentLevel);
        }
    }
    @Override
    public void exitElement(HTMLParser.ElementContext ctx)
    {
        this.lastLine = ctx.stop.getLine();
    }

    @Override
    public void exitHtmlDocument(HTMLParser.HtmlDocumentContext ctx)
    {
        // Set indentation level to 0 for all blank lines after the end of an html document
        if (this.lastLine < this.document.getParagraphs().size())
        {
            for (int i = this.lastLine; i < this.document.getParagraphs().size(); i++)
                if (!this.indentMap.containsKey(i))
                    this.indentMap.put(i, 0);
        }
        // Use the indentation of previous line for blank lines that didn't have any html elements on them
        for (int i = 0; i < this.document.getParagraphs().size(); i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> paragraph = this.document.getParagraph(i);
            if (this.indentMap.containsKey(i))
            {
                // If it already has that line in the indentMap but then like is blank/empty, remove it.
                if (paragraph.getText().isEmpty() || paragraph.getText().matches("^\\s+$"))
                    this.indentMap.remove(i);
                else
                    continue;
            }

            if (this.indentMap.containsKey(i - 1))
                this.indentMap.put(i, this.indentMap.get(i - 1));
            else
                this.indentMap.put(i, 0);
        }
    }


    @Override
    public void compute(StyledDocument<Collection<String>, String, Collection<String>> document)
    {
        this.lastLine = 0;
        this.document = document;
        this.indentLevel = -1;
        this.indentMap = new HashMap<>();
        lexer.setInputStream(CharStreams.fromString(document.getText()));
        parser.setTokenStream(new CommonTokenStream(lexer));
        this.walker.walk(this, parser.htmlDocument());
    }



    @Override
    public int indentLevelForLine(int lineNumber)
    {
        if (lineNumber < 0)
            throw new RuntimeException(String.format("Line number %d provided to HTMLIndentationMap is invalid.", lineNumber));
        if (indentMap.containsKey(lineNumber))
            return indentMap.get(lineNumber);
        System.err.println(String.format("No Indentation found for line %d", lineNumber));
        return 0;
    }

    private boolean canSetLineIndentation(int lineNumber)
    {
        if (this.indentMap.containsKey(lineNumber) ||
                lineNumber > this.document.getParagraphs().size()||
                lineNumber < 0)
            return false;
        return true;
    }

    private boolean isValidLineNumber(int lineNumber)
    {
        if (lineNumber > this.document.getParagraphs().size() || lineNumber < 0)
            return false;
        return true;
    }
}
