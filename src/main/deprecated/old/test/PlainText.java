package io.orbit.text.test;

import io.orbit.api.highlighting.ANTLRHighlightingProvider;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.LanguageDelegate;
import io.orbit.api.highlighting.HighlightingProvider;
import io.orbit.text.antlr.plaintext.PlainTextLexer;
import io.orbit.text.antlr.plaintext.PlainTextParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;


/**
 * Created by Tyler Swann on Friday February 16, 2018 at 14:28
 */
@Deprecated
public class PlainText implements LanguageDelegate
{


    @Override
    public HighlightingProvider getHighlightingProvider()
    {
        return new EmptyHighlightingProvider();
    }


    @Override
    public String getFileNameExtension() { return "txt"; }

    private static class EmptyHighlightingProvider implements ANTLRHighlightingProvider
    {
        private static final PlainTextLexer lexer;
        private static final PlainTextParser parser;

        static {
            lexer = new PlainTextLexer(CharStreams.fromString(""));
            parser = new PlainTextParser(new CommonTokenStream(lexer));
        }
        @Override
        public Parser getParser() { return parser; }
        @Override
        public Lexer getLexer() { return lexer; }
        @Override
        public HighlightType getHighlightType(TerminalNode node) { return HighlightType.EMPTY; }
        @Override
        public HighlightType getHighlightType(ParserRuleContext context) { return HighlightType.EMPTY; }
        @Override
        public ParserRuleContext getPrimaryExpression(String source)
        {
            lexer.setInputStream(CharStreams.fromString(source));
            parser.setTokenStream(new CommonTokenStream(lexer));
            return parser.document();
        }
    }
}
