// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\text\antlr\SampleLanguage.g4 by ANTLR 4.7
 package io.orbit.text.antlr; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SampleLanguageParser}.
 */
public interface SampleLanguageListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SampleLanguageParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SampleLanguageParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(SampleLanguageParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(SampleLanguageParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(SampleLanguageParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(SampleLanguageParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(SampleLanguageParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(SampleLanguageParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SampleLanguageParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SampleLanguageParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#accessModifier}.
	 * @param ctx the parse tree
	 */
	void enterAccessModifier(SampleLanguageParser.AccessModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#accessModifier}.
	 * @param ctx the parse tree
	 */
	void exitAccessModifier(SampleLanguageParser.AccessModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(SampleLanguageParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(SampleLanguageParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(SampleLanguageParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(SampleLanguageParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(SampleLanguageParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(SampleLanguageParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(SampleLanguageParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(SampleLanguageParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SampleLanguageParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SampleLanguageParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#conditionalOperator}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOperator(SampleLanguageParser.ConditionalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#conditionalOperator}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOperator(SampleLanguageParser.ConditionalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SampleLanguageParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(SampleLanguageParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SampleLanguageParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(SampleLanguageParser.CommentContext ctx);
}