package io.orbit.webtools;

import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.antlr.html.antlr.HTMLLexer;
import io.orbit.webtools.antlr.html.antlr.HTMLParser;
import io.orbit.webtools.antlr.html.HTMLParserBaseListener;
import io.orbit.api.formatting.IndentationMap;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.fxmisc.richtext.model.StyledDocument;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Sunday April 22, 2018 at 13:30
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

    private int indentLevel = 0;
    private Map<Integer, Integer> indentMap = new HashMap<>();
    private final ParseTreeWalker walker = new ParseTreeWalker();

    @Override
    public void enterElement(HTMLParser.ElementContext ctx)
    {

        int bracketStartLine = ctx.start.getLine() - 1;
        int bracketEndLine = ctx.stop.getLine() - 1;
        int innerHtmlStartLine = bracketStartLine + 1;
        int innerHtmlEndLine = bracketEndLine - 1;

        //System.out.println(String.format("%d -> %d", bracketStartLine, bracketEndLine));

        if (!this.indentMap.keySet().contains(bracketStartLine) && !this.indentMap.keySet().contains(bracketEndLine))
        {
            this.indentMap.put(bracketStartLine, indentLevel);
            this.indentMap.put(bracketEndLine, indentLevel);
        }
        if (bracketStartLine == bracketEndLine)
        {
            this.indentMap.put(innerHtmlStartLine, this.indentLevel);
            return;
        }
        this.indentLevel++;
        for (int i = innerHtmlStartLine; i <= innerHtmlEndLine; i++)
        {
            if (this.indentMap.containsKey(i))
                this.indentMap.remove(i);
            this.indentMap.put(i, this.indentLevel);
        }
    }

    @Override
    public void exitElement(HTMLParser.ElementContext ctx)
    {
        this.indentLevel--;
    }

    @Override
    public void exitHtmlDocument(HTMLParser.HtmlDocumentContext ctx)
    {
        for (int i = 0; i < this.document.getParagraphs().size(); i++)
        {
            if (!this.indentMap.containsKey(i))
                this.indentMap.put(i, 0);
        }
    }


    @Override
    public void compute(StyledDocument<Collection<String>, String, Collection<String>> document)
    {
        this.document = document;
        this.indentLevel = 0;
        this.indentMap = new HashMap<>();
        lexer.setInputStream(CharStreams.fromString(document.getText()));
        parser.setTokenStream(new CommonTokenStream(lexer));
        this.walker.walk(this, parser.htmlDocument());
//        Platform.runLater(() -> {
//            for (int line : this.indentMap.keySet())
//            {
//                System.out.println(String.format("Line: %d -> %d", line, this.indentMap.get(line)));
//            }
//        });
    }

    void debugIndentation(CodeEditor editor)
    {
        editor.caretPositionProperty().addListener(event -> {
            System.out.println("Indent Level: " + this.indentLevelForLine(editor.getFocusPosition().line));
        });
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
}
