// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\text\antlr\plaintext\PlainText.g4 by ANTLR 4.7
 package io.orbit.text.antlr.plaintext; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PlainTextParser}.
 */
public interface PlainTextListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PlainTextParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(PlainTextParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlainTextParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(PlainTextParser.DocumentContext ctx);
}