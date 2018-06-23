package io.orbit.webtools;

//import io.orbit.api.highlighting.HighlightType;
//import io.orbit.api.highlighting.SyntaxHighlighter;
//import io.orbit.webtools.antlr.html.antlr.HTMLLexer;
//import io.orbit.webtools.antlr.html.antlr.HTMLParser;
//import io.orbit.webtools.antlr.html.antlr.HTMLParserBaseListener;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.Token;
//import org.antlr.v4.runtime.tree.ParseTreeWalker;
//import org.antlr.v4.runtime.tree.TerminalNode;
//import org.fxmisc.richtext.model.StyleSpans;
//import org.fxmisc.richtext.model.StyleSpansBuilder;
//
//import java.time.Duration;
//import java.util.Collection;
//import java.util.Collections;

/**
 * Created by Tyler Swann on Friday April 27, 2018 at 14:16
 */
public class HTMLANTLRSyntaxHighlighter// extends HTMLParserBaseListener implements SyntaxHighlighter
{
//    private static final HTMLParser parser;
//    private static final HTMLLexer lexer;
//    private static final ParseTreeWalker walker = new ParseTreeWalker();
//
//    static {
//        lexer = new HTMLLexer(CharStreams.fromString(""));
//        parser = new HTMLParser(new CommonTokenStream(lexer));
//        lexer.getErrorListeners().clear();
//        parser.getErrorListeners().clear();
//        parser.setBuildParseTree(true);
//    }
//
//    private int lastEnd = 0;
//    private StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
//    private StyleSpans<Collection<String>> styles;
//
//    @Override
//    public StyleSpans<Collection<String>> computeHighlighting(String text)
//    {
//        lexer.setInputStream(CharStreams.fromString(text));
//        parser.setInputStream(new CommonTokenStream(lexer));
//        walker.walk(this, parser.htmlDocument());
//        return this.getStyles();
//    }
//
//   @Override
//   public Duration getHighlightingInterval()
//   {
//       return Duration.ofMillis(50);
//   }
//
//
//    @Override
//    public void enterHtmlDocument(HTMLParser.HtmlDocumentContext ctx)
//    {
//        this.lastEnd = 0;
//        this.builder = new StyleSpansBuilder<>();
//        this.styles = null;
//    }
//    @Override public void enterHtmlElements(HTMLParser.HtmlElementsContext ctx)
//    {
//
//    }
//    @Override public void enterElement(HTMLParser.ElementContext ctx)
//    {
//        System.out.println(ctx.TAG_OPEN(0).getSymbol().getLine()  +" -> " + ctx.TAG_CLOSE(0).getSymbol().getLine());
//    }
//    @Override
//    public void enterHtmlTagName(HTMLParser.HtmlTagNameContext ctx)
//    {
//        this.addToken(ctx.TAG_NAME(), HighlightType.ANNOTATION);
//    }
//    @Override
//    public void enterHtmlComment(HTMLParser.HtmlCommentContext ctx)
//    {
//        this.addToken(ctx.HTML_COMMENT(), HighlightType.BLOCK_COMMENT);
//    }
//    @Override
//    public void enterHtmlAttribute(HTMLParser.HtmlAttributeContext ctx)
//    {
//        this.addToken(ctx.htmlAttributeName().TAG_NAME(), HighlightType.ANNOTATION);
//        this.addToken(ctx.htmlAttributeValue().ATTVALUE_VALUE(), HighlightType.STRING);
//    }
//
//    @Override public void enterSelfClosingElement(HTMLParser.SelfClosingElementContext ctx)
//    {
//
//    }
//    @Override public void enterVoidElement(HTMLParser.VoidElementContext ctx)
//    {
//
//    }
//
//
//
//    private void addToken(TerminalNode node, HighlightType type)
//    {
//        if (node == null || type == HighlightType.EMPTY)
//            return;
//        Token token = node.getSymbol();
//        int spanEnd = token.getStartIndex() - lastEnd;
//        if (spanEnd > 0)
//        {
//            builder.add(Collections.emptyList(), spanEnd);
//            int length = token.getText().length();
//            builder.add(Collections.singleton(type.className), length);
//            lastEnd = token.getStopIndex() + 1;
//        }
//    }
//
//    private StyleSpans<Collection<String>> getStyles()
//    {
//        if (styles == null)
//            styles = builder.create();
//        return styles;
//    }

}
