package io.orbit.webtools;

import io.orbit.api.highlighting.ANTLRHighlightingProvider;
import io.orbit.api.highlighting.HighlightType;
import io.orbit.webtools.antlr.css.CSS3Lexer;
import io.orbit.webtools.antlr.css.CSS3Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 18:08
 */
@Deprecated
public class CSS3HighlightingProvider implements ANTLRHighlightingProvider
{
    private static final CSS3Lexer lexer;
    private static final CSS3Parser parser;

    static {
        lexer = new CSS3Lexer(CharStreams.fromString(""));
        parser = new CSS3Parser(new CommonTokenStream(lexer));
    }

    @Override
    public Parser getParser() { return parser; }

    @Override
    public Lexer getLexer() { return lexer; }

    @Override
    public HighlightType getHighlightType(TerminalNode node)
    {
        HighlightType type = HighlightType.EMPTY;
        int nodeType = node.getSymbol().getType();
        switch (nodeType)
        {
            case CSS3Lexer.String: type = HighlightType.STRING; break;
            case CSS3Lexer.Comment: type = HighlightType.BLOCK_COMMENT; break;
            case CSS3Lexer.Number: type = HighlightType.NUMBER; break;
            case CSS3Lexer.Important:
            case CSS3Lexer.Percentage:
            case CSS3Lexer.Page:
            case CSS3Lexer.Media:
            case CSS3Lexer.Namespace:
            case CSS3Lexer.Charset:
            case CSS3Lexer.FontFace:
            case CSS3Lexer.Supports:
            case CSS3Lexer.Keyframes:
            case CSS3Lexer.Viewport:
            case CSS3Lexer.CounterStyle:
            case CSS3Lexer.FontFeatureValues:
            case CSS3Lexer.Import: type = HighlightType.KEYWORD; break;
            case CSS3Lexer.Plus:
            case CSS3Lexer.Minus:
            case CSS3Lexer.Greater:
            case CSS3Lexer.Comma:
            case CSS3Lexer.Tilde:
            case CSS3Lexer.PseudoNot:
            case CSS3Lexer.PrefixMatch:
            case CSS3Lexer.SuffixMatch:
            case CSS3Lexer.Includes:
            case CSS3Lexer.DashMatch:
            case CSS3Lexer.SubstringMatch: type = HighlightType.OPERATOR; break;
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
            case CSS3Parser.RULE_selectorGroup: type = HighlightType.TYPE; break;
            case CSS3Parser.RULE_property: type = HighlightType.ANNOTATION; break;
            case CSS3Parser.RULE_hexcolor: type = HighlightType.KEYWORD; break;
        }
        return type;
    }

    @Override
    public ParserRuleContext getPrimaryExpression(String source)
    {
        lexer.setInputStream(CharStreams.fromString(source));
        parser.setTokenStream(new CommonTokenStream(lexer));
        return parser.stylesheet();
    }
}
