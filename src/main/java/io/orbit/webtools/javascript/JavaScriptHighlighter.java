package io.orbit.webtools.javascript;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.SyntaxHighlighter;
import io.orbit.webtools.javascript.antlr.JavaScriptLexer;
import io.orbit.webtools.javascript.antlr.JavaScriptParser;
import io.orbit.webtools.javascript.antlr.JavaScriptParserBaseListener;
import io.orbit.webtools.javascript.antlr.JavaScriptParserListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class JavaScriptHighlighter extends JavaScriptParserBaseListener implements SyntaxHighlighter
{
    private static final ArrayList<Integer> KEYWORDS = new ArrayList<>(Arrays.asList(
            JavaScriptLexer.Function,
            JavaScriptLexer.Continue,
            JavaScriptLexer.Var,
            JavaScriptLexer.Let,
            JavaScriptLexer.While,
            JavaScriptLexer.For,
            JavaScriptLexer.Const,
            JavaScriptLexer.If,
            JavaScriptLexer.Else,
            JavaScriptLexer.Return,
            JavaScriptLexer.Break,
            JavaScriptLexer.BooleanLiteral,
            JavaScriptLexer.This,
            JavaScriptLexer.Class,
            JavaScriptLexer.Switch,
            JavaScriptLexer.Default,
            JavaScriptLexer.Case,
            JavaScriptLexer.Catch,
            JavaScriptLexer.New,
            JavaScriptLexer.Try,
            JavaScriptLexer.NullLiteral
    ));
    private static final ArrayList<Integer> NUMBERS = new ArrayList<>(Arrays.asList(
            JavaScriptLexer.HexIntegerLiteral,
            JavaScriptLexer.OctalIntegerLiteral,
            JavaScriptLexer.BinaryIntegerLiteral,
            JavaScriptLexer.OctalIntegerLiteral2,
            JavaScriptLexer.DecimalLiteral
    ));
    private static final ArrayList<Integer> OPERATORS = new ArrayList<>(Arrays.asList(
            JavaScriptLexer.OpenBracket,
            JavaScriptLexer.CloseBracket,
            JavaScriptLexer.OpenParen,
            JavaScriptLexer.CloseParen,
            JavaScriptLexer.OpenBrace,
            JavaScriptLexer.CloseBrace,
            JavaScriptLexer.SemiColon,
            JavaScriptLexer.Comma,
            JavaScriptLexer.Assign,
            JavaScriptLexer.QuestionMark,
            JavaScriptLexer.Colon,
            JavaScriptLexer.Ellipsis,
            JavaScriptLexer.Dot,
            JavaScriptLexer.PlusPlus,
            JavaScriptLexer.MinusMinus,
            JavaScriptLexer.Plus,
            JavaScriptLexer.Minus,
            JavaScriptLexer.BitNot,
            JavaScriptLexer.Not,
            JavaScriptLexer.Multiply,
            JavaScriptLexer.Divide,
            JavaScriptLexer.Modulus,
            JavaScriptLexer.RightShiftArithmetic,
            JavaScriptLexer.LeftShiftArithmetic,
            JavaScriptLexer.RightShiftLogical,
            JavaScriptLexer.LessThan,
            JavaScriptLexer.MoreThan,
            JavaScriptLexer.LessThanEquals,
            JavaScriptLexer.GreaterThanEquals,
            JavaScriptLexer.Equals_,
            JavaScriptLexer.NotEquals,
            JavaScriptLexer.IdentityEquals,
            JavaScriptLexer.IdentityNotEquals,
            JavaScriptLexer.BitAnd,
            JavaScriptLexer.BitXOr,
            JavaScriptLexer.BitOr,
            JavaScriptLexer.And,
            JavaScriptLexer.Or,
            JavaScriptLexer.MultiplyAssign,
            JavaScriptLexer.DivideAssign,
            JavaScriptLexer.ModulusAssign,
            JavaScriptLexer.PlusAssign,
            JavaScriptLexer.MinusAssign,
            JavaScriptLexer.LeftShiftArithmeticAssign,
            JavaScriptLexer.RightShiftArithmeticAssign,
            JavaScriptLexer.RightShiftLogicalAssign,
            JavaScriptLexer.BitAndAssign,
            JavaScriptLexer.BitXorAssign,
            JavaScriptLexer.BitOrAssign,
            JavaScriptLexer.ARROW
    ));


    private StyleSpansBuilder<Collection<String>> styles;
    private int lastEnd = 0;
    private JavaScriptLexer lexer;
    private JavaScriptParser parser;

    @Override
    public StyleSpans<Collection<String>> computeHighlighting(String text)
    {
        this.styles = new StyleSpansBuilder<>();
        this.lastEnd = 0;
        this.walk(text);
        return this.styles.create();
    }

    @Override
    public void visitTerminal(TerminalNode terminalNode)
    {
        if (terminalNode == null)
            return;

        if (KEYWORDS.contains(terminalNode.getSymbol().getType()))
            addStyle(terminalNode, HighlightType.KEYWORD);
        else if (terminalNode.getSymbol().getType() == JavaScriptLexer.StringLiteral)
            addStyle(terminalNode, HighlightType.STRING);
        else if (terminalNode.getSymbol().getType() == JavaScriptLexer.SemiColon)
            addStyle(terminalNode, HighlightType.SEMI_COLON);
    }

    @Override
    public void enterComment(JavaScriptParser.CommentContext context)
    {
        if (context.MultiLineComment() != null)
            addStyle(context.MultiLineComment(), HighlightType.BLOCK_COMMENT);
        else if (context.SingleLineComment() != null)
            addStyle(context.SingleLineComment(), HighlightType.LINE_COMMENT);
    }

    @Override
    public void enterFunctionDeclaration(JavaScriptParser.FunctionDeclarationContext context)
    {
        addStyle(context.Function(), HighlightType.KEYWORD);
        addStyle(context.Identifier(), HighlightType.FUNCTION_NAME);
    }

    @Override
    public void enterNumericLiteral(JavaScriptParser.NumericLiteralContext context)
    {
        for (Integer NUMBER : NUMBERS)
            context.getTokens(NUMBER).forEach(node -> addStyle(node, HighlightType.NUMBER));
    }


    @Override
    public void enterArgumentsExpression(JavaScriptParser.ArgumentsExpressionContext context)
    {
        if (context.singleExpression() instanceof JavaScriptParser.IdentifierExpressionContext)
        {
            TerminalNode idNode = ((JavaScriptParser.IdentifierExpressionContext) context.singleExpression()).Identifier();
            addStyle(idNode, HighlightType.FUNCTION_NAME);
        }
        else if (context.singleExpression() instanceof JavaScriptParser.MemberDotExpressionContext)
        {

            TerminalNode idNode = ((JavaScriptParser.MemberDotExpressionContext)context.singleExpression()).identifierName().Identifier();
            addStyle(idNode, HighlightType.FUNCTION_NAME);
        }
    }


    private void walk(String text)
    {
        if (this.lexer == null)
        {
            lexer = new JavaScriptLexer(CharStreams.fromString(text));
            parser = new JavaScriptParser(new CommonTokenStream(lexer));
//            lexer.getErrorListeners().clear();
//            parser.getErrorListeners().clear();
        }
        else
        {
            this.lexer.setInputStream(CharStreams.fromString(text));
            this.parser.setInputStream(new CommonTokenStream(lexer));
        }
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, this.parser.program());
    }

    private void addStyle(TerminalNode node, HighlightType type)
    {
        Token token = node.getSymbol();
        int space = token.getStartIndex() - lastEnd;
        if (space > 0)
        {
            this.styles.add(Collections.emptyList(), space);
            int gap = token.getText().length();
            this.styles.add(Collections.singleton(type.className), gap);
            this.lastEnd = token.getStopIndex() + 1;
        }
    }
}
