package io.orbit.text.test;

import io.orbit.api.highlighting.ANTLRHighlightingProvider;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.text.antlr.SampleLanguageLexer;
import io.orbit.text.antlr.SampleLanguageParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 18:10
 */
@Deprecated
public class SampleHighlightingProvider implements ANTLRHighlightingProvider
{
    private static final SampleLanguageParser parser;
    private static final SampleLanguageLexer lexer;

    static {
        lexer = new SampleLanguageLexer(CharStreams.fromString(""));
        parser = new SampleLanguageParser(new CommonTokenStream(lexer));
    }

    @Override
    public SampleLanguageParser getParser() { return parser; }
    @Override
    public SampleLanguageLexer getLexer() { return lexer; }

    public ParserRuleContext getPrimaryExpression(String source)
    {
        lexer.setInputStream(CharStreams.fromString(source));
        parser.setTokenStream(new CommonTokenStream(lexer));
        return parser.program();
    }

    @Override
    public HighlightType getHighlightType(TerminalNode node)
    {
        HighlightType type = HighlightType.EMPTY;
        int nodeType = node.getSymbol().getType();
        switch (nodeType)
        {
            case SampleLanguageParser.VAR:
            case SampleLanguageParser.LET:
            case SampleLanguageParser.FUNC:
            case SampleLanguageParser.BOOL: type = HighlightType.KEYWORD; break;
            case SampleLanguageParser.STRING: type = HighlightType.STRING; break;
            case SampleLanguageParser.BLOCK_COMMENT: type = HighlightType.BLOCK_COMMENT; break;
            case SampleLanguageParser.LINE_COMMENT: type = HighlightType.LINE_COMMENT; break;
            case SampleLanguageParser.INT:
            case SampleLanguageParser.LONG:
            case SampleLanguageParser.FLOAT:
            case SampleLanguageParser.DOUBLE:
                type = HighlightType.NUMBER;
        }
        return type;
    }

    @Override
    public HighlightType getHighlightType(ParserRuleContext context)
    {
        HighlightType type = HighlightType.EMPTY;
        int ruleType = context.getRuleIndex();
        switch (ruleType)
        {
            case SampleLanguageParser.RULE_type: type = HighlightType.TYPE; break;
            case SampleLanguageParser.RULE_accessModifier: type = HighlightType.KEYWORD; break;
            default: break;
        }
        return type;
    }
}
