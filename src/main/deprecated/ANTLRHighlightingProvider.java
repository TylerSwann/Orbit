package io.orbit.api.highlighting;

import io.orbit.api.NotNullable;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by Tyler Swann on Thursday April 05, 2018 at 16:28
 */
@Deprecated
public interface ANTLRHighlightingProvider extends HighlightingProvider
{
    /**
     *
     * @return - An instance of the ANTLR Parser for your language
     */
    @NotNullable
    Parser getParser();

    /**
     *
     * @return - An instance of the ANTLR Lexer for your language
     */
    @NotNullable
    Lexer getLexer();

    /**
     *
     * Using the TerminalNode.getSymbol().getType() method, switch through the
     * Lexer rule types and identify what type of lexer rule node is. Based on that return
     * what HighlightType you want that node highlighted as.
     * switch through these using ONLY LEXER RULES. Using parser rules for this method
     * will potentially lead to incorrect highlighting as some Parser rule indexes are identical to
     * Lexer rule types. For info on highlighting parser rules, see getHighlightType(ParserRuleContext context)
     * example:
     * int type = node.getSymbol().getType();
     * switch (type)
     * {
     *     case MyLexer.String:
     *          return HighlightType.STRING;
     *     default:
     *          // Under the circumstances the TerminalNode's type is irrelevant or doesn't require highlighting.
     *          return HighlightType.EMPTY;
     * }
     *
     * @param node - TerminalNode
     * @return
     */
    @NotNullable
    HighlightType getHighlightType(TerminalNode node);
    /**
     *
     * Using the ParserRuleContext.getRuleIndex(); method, switch through the
     * Parser rule types and identify what parser rule index context is. Based on that return
     * what HighlightType you want that rule highlighted as.
     * switch through these using ONLY PARSER RULES. Using Lexer rules for this method
     * will potentially lead to incorrect highlighting as some Lexer rule types are identical to
     * Parser rule indexes. For info on highlighting lexer rules, see getHighlightType(TerminalNode node)
     * example:
     * int type = context.getRuleIndex();
     * switch (type)
     * {
     *     case Parser.Rule_methodName:
     *          return HighlightType.INSTANCE_METHOD;
     *     default:
     *          // Under the circumstances the rule index is irrelevant or doesn't require highlighting.
     *          return HighlightType.EMPTY;
     * }
     *
     * @param node - TerminalNode
     * @return
     */
    @NotNullable
    HighlightType getHighlightType(ParserRuleContext context);

    /**
     * Return the primary expression generated by your Parser. The primary expression is the
     * first Parser rule that can be accessed.
     * example:
     *
     *         CSS3Lexer lexer = new CSS3Lexer(CharStreams.fromString(source));
     *         CSS3Parser parser = new CSS3Parser(new CommonTokenStream(lexer));
     *         return parser.stylesheet();
     *
     * @param source - The source code that is in need of parsing
     * @return - The primary ParserRuleContext
     */
    @NotNullable
    ParserRuleContext getPrimaryExpression(String source);
}
