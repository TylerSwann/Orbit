// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\webtools\antlr\html\HTML.g4 by ANTLR 4.7
  package io.orbit.webtools.antlr.html;  
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HTMLParser}.
 */
public interface HTMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HTMLParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(HTMLParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(HTMLParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterHtmlElement(HTMLParser.HtmlElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitHtmlElement(HTMLParser.HtmlElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#voidElement}.
	 * @param ctx the parse tree
	 */
	void enterVoidElement(HTMLParser.VoidElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#voidElement}.
	 * @param ctx the parse tree
	 */
	void exitVoidElement(HTMLParser.VoidElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(HTMLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(HTMLParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#selfClosingElement}.
	 * @param ctx the parse tree
	 */
	void enterSelfClosingElement(HTMLParser.SelfClosingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#selfClosingElement}.
	 * @param ctx the parse tree
	 */
	void exitSelfClosingElement(HTMLParser.SelfClosingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#doctype}.
	 * @param ctx the parse tree
	 */
	void enterDoctype(HTMLParser.DoctypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#doctype}.
	 * @param ctx the parse tree
	 */
	void exitDoctype(HTMLParser.DoctypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#openTag}.
	 * @param ctx the parse tree
	 */
	void enterOpenTag(HTMLParser.OpenTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#openTag}.
	 * @param ctx the parse tree
	 */
	void exitOpenTag(HTMLParser.OpenTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#closeTag}.
	 * @param ctx the parse tree
	 */
	void enterCloseTag(HTMLParser.CloseTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#closeTag}.
	 * @param ctx the parse tree
	 */
	void exitCloseTag(HTMLParser.CloseTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#voidTagName}.
	 * @param ctx the parse tree
	 */
	void enterVoidTagName(HTMLParser.VoidTagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#voidTagName}.
	 * @param ctx the parse tree
	 */
	void exitVoidTagName(HTMLParser.VoidTagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(HTMLParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(HTMLParser.ContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#xhtmlCDATA}.
	 * @param ctx the parse tree
	 */
	void enterXhtmlCDATA(HTMLParser.XhtmlCDATAContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#xhtmlCDATA}.
	 * @param ctx the parse tree
	 */
	void exitXhtmlCDATA(HTMLParser.XhtmlCDATAContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(HTMLParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(HTMLParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HTMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(HTMLParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link HTMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(HTMLParser.CommentContext ctx);
}