// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\webtools\javascript\typedefinitions\grammars\TypeDefinition.g4 by ANTLR 4.7
 package io.orbit.webtools.javascript.typedefinitions.grammars; 
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TypeDefinitionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TypeDefinitionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#typeDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDefinition(TypeDefinitionParser.TypeDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#typeInterface}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeInterface(TypeDefinitionParser.TypeInterfaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyType(TypeDefinitionParser.PropertyTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionType(TypeDefinitionParser.FunctionTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NonVoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonVoidFunction(TypeDefinitionParser.NonVoidFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidFunction(TypeDefinitionParser.VoidFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(TypeDefinitionParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StandardArgType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandardArgType(TypeDefinitionParser.StandardArgTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarArgsType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarArgsType(TypeDefinitionParser.VarArgsTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#declaredVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaredVar(TypeDefinitionParser.DeclaredVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(TypeDefinitionParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(TypeDefinitionParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CustomType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomType(TypeDefinitionParser.CustomTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#localName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalName(TypeDefinitionParser.LocalNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypeDefinitionParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(TypeDefinitionParser.NameContext ctx);
}