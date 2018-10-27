// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\webtools\javascript\typedefinitions\grammars\TypeDefinition.g4 by ANTLR 4.7
 package io.orbit.webtools.javascript.typedefinitions.grammars; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TypeDefinitionParser}.
 */
public interface TypeDefinitionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(TypeDefinitionParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(TypeDefinitionParser.TypeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#typeInterface}.
	 * @param ctx the parse tree
	 */
	void enterTypeInterface(TypeDefinitionParser.TypeInterfaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#typeInterface}.
	 * @param ctx the parse tree
	 */
	void exitTypeInterface(TypeDefinitionParser.TypeInterfaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 */
	void enterPropertyType(TypeDefinitionParser.PropertyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 */
	void exitPropertyType(TypeDefinitionParser.PropertyTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 */
	void enterFunctionType(TypeDefinitionParser.FunctionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionType}
	 * labeled alternative in {@link TypeDefinitionParser#interfaceMember}.
	 * @param ctx the parse tree
	 */
	void exitFunctionType(TypeDefinitionParser.FunctionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NonVoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 */
	void enterNonVoidFunction(TypeDefinitionParser.NonVoidFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NonVoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 */
	void exitNonVoidFunction(TypeDefinitionParser.NonVoidFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 */
	void enterVoidFunction(TypeDefinitionParser.VoidFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidFunction}
	 * labeled alternative in {@link TypeDefinitionParser#functionalTypeDef}.
	 * @param ctx the parse tree
	 */
	void exitVoidFunction(TypeDefinitionParser.VoidFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(TypeDefinitionParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(TypeDefinitionParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StandardArgType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterStandardArgType(TypeDefinitionParser.StandardArgTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StandardArgType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitStandardArgType(TypeDefinitionParser.StandardArgTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarArgsType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterVarArgsType(TypeDefinitionParser.VarArgsTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarArgsType}
	 * labeled alternative in {@link TypeDefinitionParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitVarArgsType(TypeDefinitionParser.VarArgsTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#declaredVar}.
	 * @param ctx the parse tree
	 */
	void enterDeclaredVar(TypeDefinitionParser.DeclaredVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#declaredVar}.
	 * @param ctx the parse tree
	 */
	void exitDeclaredVar(TypeDefinitionParser.DeclaredVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(TypeDefinitionParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(TypeDefinitionParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimitiveType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(TypeDefinitionParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimitiveType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(TypeDefinitionParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CustomType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void enterCustomType(TypeDefinitionParser.CustomTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CustomType}
	 * labeled alternative in {@link TypeDefinitionParser#type}.
	 * @param ctx the parse tree
	 */
	void exitCustomType(TypeDefinitionParser.CustomTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#localName}.
	 * @param ctx the parse tree
	 */
	void enterLocalName(TypeDefinitionParser.LocalNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#localName}.
	 * @param ctx the parse tree
	 */
	void exitLocalName(TypeDefinitionParser.LocalNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypeDefinitionParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(TypeDefinitionParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypeDefinitionParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(TypeDefinitionParser.NameContext ctx);
}