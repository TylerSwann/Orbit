// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\webtools\javascript\typedefinitions\grammars\TypeDefinition.g4 by ANTLR 4.7
 package io.orbit.webtools.javascript.typedefinitions.grammars; 
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeDefinitionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, STRING=14, BOOL=15, NUMBER=16, 
		ANY=17, VOID=18, READONLY=19, QUESTION_MARK=20, IDENTIFIER=21, WhiteSpaces=22, 
		LineTerminator=23;
	public static final int
		RULE_typeDefinition = 0, RULE_typeInterface = 1, RULE_interfaceMember = 2, 
		RULE_functionalTypeDef = 3, RULE_argumentList = 4, RULE_argument = 5, 
		RULE_declaredVar = 6, RULE_type = 7, RULE_localName = 8, RULE_name = 9;
	public static final String[] ruleNames = {
		"typeDefinition", "typeInterface", "interfaceMember", "functionalTypeDef", 
		"argumentList", "argument", "declaredVar", "type", "localName", "name"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'interface'", "'extends'", "'{'", "'}'", "';'", "'('", "')'", "':'", 
		"','", "'...'", "'declare'", "'var'", "'[]'", "'string'", "'boolean'", 
		"'number'", "'any'", "'void'", "'readonly'", "'?'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "STRING", "BOOL", "NUMBER", "ANY", "VOID", "READONLY", "QUESTION_MARK", 
		"IDENTIFIER", "WhiteSpaces", "LineTerminator"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TypeDefinition.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TypeDefinitionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TypeDefinitionContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TypeDefinitionParser.EOF, 0); }
		public List<TypeInterfaceContext> typeInterface() {
			return getRuleContexts(TypeInterfaceContext.class);
		}
		public TypeInterfaceContext typeInterface(int i) {
			return getRuleContext(TypeInterfaceContext.class,i);
		}
		public List<DeclaredVarContext> declaredVar() {
			return getRuleContexts(DeclaredVarContext.class);
		}
		public DeclaredVarContext declaredVar(int i) {
			return getRuleContext(DeclaredVarContext.class,i);
		}
		public TypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterTypeDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitTypeDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDefinitionContext typeDefinition() throws RecognitionException {
		TypeDefinitionContext _localctx = new TypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_typeDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==T__10) {
				{
				setState(22);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__0:
					{
					setState(20);
					typeInterface();
					}
					break;
				case T__10:
					{
					setState(21);
					declaredVar();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(27);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeInterfaceContext extends ParserRuleContext {
		public NameContext parent;
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public List<InterfaceMemberContext> interfaceMember() {
			return getRuleContexts(InterfaceMemberContext.class);
		}
		public InterfaceMemberContext interfaceMember(int i) {
			return getRuleContext(InterfaceMemberContext.class,i);
		}
		public TypeInterfaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeInterface; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterTypeInterface(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitTypeInterface(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitTypeInterface(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeInterfaceContext typeInterface() throws RecognitionException {
		TypeInterfaceContext _localctx = new TypeInterfaceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_typeInterface);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			match(T__0);
			setState(30);
			name();
			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(31);
				match(T__1);
				setState(32);
				((TypeInterfaceContext)_localctx).parent = name();
				}
			}

			setState(35);
			match(T__2);
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__9 || _la==IDENTIFIER) {
				{
				{
				setState(36);
				interfaceMember();
				}
				}
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(42);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMemberContext extends ParserRuleContext {
		public InterfaceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMember; }
	 
		public InterfaceMemberContext() { }
		public void copyFrom(InterfaceMemberContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionTypeContext extends InterfaceMemberContext {
		public FunctionalTypeDefContext functionalTypeDef() {
			return getRuleContext(FunctionalTypeDefContext.class,0);
		}
		public FunctionTypeContext(InterfaceMemberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterFunctionType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitFunctionType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitFunctionType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PropertyTypeContext extends InterfaceMemberContext {
		public ArgumentContext argument() {
			return getRuleContext(ArgumentContext.class,0);
		}
		public PropertyTypeContext(InterfaceMemberContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterPropertyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitPropertyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitPropertyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InterfaceMemberContext interfaceMember() throws RecognitionException {
		InterfaceMemberContext _localctx = new InterfaceMemberContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_interfaceMember);
		try {
			setState(48);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new PropertyTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(44);
				argument();
				setState(45);
				match(T__4);
				}
				}
				break;
			case 2:
				_localctx = new FunctionTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				functionalTypeDef();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionalTypeDefContext extends ParserRuleContext {
		public FunctionalTypeDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionalTypeDef; }
	 
		public FunctionalTypeDefContext() { }
		public void copyFrom(FunctionalTypeDefContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NonVoidFunctionContext extends FunctionalTypeDefContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public NonVoidFunctionContext(FunctionalTypeDefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterNonVoidFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitNonVoidFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitNonVoidFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VoidFunctionContext extends FunctionalTypeDefContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public VoidFunctionContext(FunctionalTypeDefContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterVoidFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitVoidFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitVoidFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionalTypeDefContext functionalTypeDef() throws RecognitionException {
		FunctionalTypeDefContext _localctx = new FunctionalTypeDefContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionalTypeDef);
		int _la;
		try {
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new NonVoidFunctionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				name();
				setState(51);
				match(T__5);
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__9 || _la==IDENTIFIER) {
					{
					setState(52);
					argumentList();
					}
				}

				setState(55);
				match(T__6);
				setState(56);
				match(T__7);
				setState(57);
				type();
				setState(58);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new VoidFunctionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(60);
				name();
				setState(61);
				match(T__5);
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__9 || _la==IDENTIFIER) {
					{
					setState(62);
					argumentList();
					}
				}

				setState(65);
				match(T__6);
				setState(66);
				match(T__7);
				setState(67);
				match(VOID);
				setState(68);
				match(T__4);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentListContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			argument();
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(73);
				match(T__8);
				setState(74);
				argument();
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
	 
		public ArgumentContext() { }
		public void copyFrom(ArgumentContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StandardArgTypeContext extends ArgumentContext {
		public LocalNameContext localName() {
			return getRuleContext(LocalNameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StandardArgTypeContext(ArgumentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterStandardArgType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitStandardArgType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitStandardArgType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarArgsTypeContext extends ArgumentContext {
		public LocalNameContext localName() {
			return getRuleContext(LocalNameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarArgsTypeContext(ArgumentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterVarArgsType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitVarArgsType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitVarArgsType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_argument);
		try {
			setState(89);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				_localctx = new StandardArgTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				localName();
				setState(81);
				match(T__7);
				setState(82);
				type();
				}
				break;
			case T__9:
				_localctx = new VarArgsTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				match(T__9);
				setState(85);
				localName();
				setState(86);
				match(T__7);
				setState(87);
				type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaredVarContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public DeclaredVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaredVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterDeclaredVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitDeclaredVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitDeclaredVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaredVarContext declaredVar() throws RecognitionException {
		DeclaredVarContext _localctx = new DeclaredVarContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_declaredVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(T__10);
			setState(92);
			match(T__11);
			setState(93);
			name();
			setState(94);
			match(T__7);
			setState(95);
			type();
			setState(96);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayTypeContext extends TypeContext {
		public TerminalNode STRING() { return getToken(TypeDefinitionParser.STRING, 0); }
		public TerminalNode BOOL() { return getToken(TypeDefinitionParser.BOOL, 0); }
		public TerminalNode NUMBER() { return getToken(TypeDefinitionParser.NUMBER, 0); }
		public TerminalNode ANY() { return getToken(TypeDefinitionParser.ANY, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TypeDefinitionParser.IDENTIFIER, 0); }
		public ArrayTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CustomTypeContext extends TypeContext {
		public TerminalNode IDENTIFIER() { return getToken(TypeDefinitionParser.IDENTIFIER, 0); }
		public CustomTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterCustomType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitCustomType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitCustomType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimitiveTypeContext extends TypeContext {
		public TerminalNode STRING() { return getToken(TypeDefinitionParser.STRING, 0); }
		public TerminalNode BOOL() { return getToken(TypeDefinitionParser.BOOL, 0); }
		public TerminalNode NUMBER() { return getToken(TypeDefinitionParser.NUMBER, 0); }
		public TerminalNode ANY() { return getToken(TypeDefinitionParser.ANY, 0); }
		public PrimitiveTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitPrimitiveType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_type);
		int _la;
		try {
			setState(102);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				_localctx = new ArrayTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(98);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << BOOL) | (1L << NUMBER) | (1L << ANY) | (1L << IDENTIFIER))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(99);
				match(T__12);
				}
				break;
			case 2:
				_localctx = new PrimitiveTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << BOOL) | (1L << NUMBER) | (1L << ANY))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 3:
				_localctx = new CustomTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(IDENTIFIER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TypeDefinitionParser.IDENTIFIER, 0); }
		public TerminalNode QUESTION_MARK() { return getToken(TypeDefinitionParser.QUESTION_MARK, 0); }
		public LocalNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterLocalName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitLocalName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitLocalName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalNameContext localName() throws RecognitionException {
		LocalNameContext _localctx = new LocalNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_localName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(IDENTIFIER);
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUESTION_MARK) {
				{
				setState(105);
				match(QUESTION_MARK);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TypeDefinitionParser.IDENTIFIER, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeDefinitionListener ) ((TypeDefinitionListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypeDefinitionVisitor ) return ((TypeDefinitionVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31q\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\7\2\31\n\2\f\2\16\2\34\13\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3$\n\3\3"+
		"\3\3\3\7\3(\n\3\f\3\16\3+\13\3\3\3\3\3\3\4\3\4\3\4\3\4\5\4\63\n\4\3\5"+
		"\3\5\3\5\5\58\n\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5B\n\5\3\5\3\5\3\5"+
		"\3\5\3\5\5\5I\n\5\3\6\3\6\3\6\7\6N\n\6\f\6\16\6Q\13\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\5\7\\\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t"+
		"\3\t\5\ti\n\t\3\n\3\n\5\nm\n\n\3\13\3\13\3\13\2\2\f\2\4\6\b\n\f\16\20"+
		"\22\24\2\4\4\2\20\23\27\27\3\2\20\23\2s\2\32\3\2\2\2\4\37\3\2\2\2\6\62"+
		"\3\2\2\2\bH\3\2\2\2\nJ\3\2\2\2\f[\3\2\2\2\16]\3\2\2\2\20h\3\2\2\2\22j"+
		"\3\2\2\2\24n\3\2\2\2\26\31\5\4\3\2\27\31\5\16\b\2\30\26\3\2\2\2\30\27"+
		"\3\2\2\2\31\34\3\2\2\2\32\30\3\2\2\2\32\33\3\2\2\2\33\35\3\2\2\2\34\32"+
		"\3\2\2\2\35\36\7\2\2\3\36\3\3\2\2\2\37 \7\3\2\2 #\5\24\13\2!\"\7\4\2\2"+
		"\"$\5\24\13\2#!\3\2\2\2#$\3\2\2\2$%\3\2\2\2%)\7\5\2\2&(\5\6\4\2\'&\3\2"+
		"\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*,\3\2\2\2+)\3\2\2\2,-\7\6\2\2-\5\3"+
		"\2\2\2./\5\f\7\2/\60\7\7\2\2\60\63\3\2\2\2\61\63\5\b\5\2\62.\3\2\2\2\62"+
		"\61\3\2\2\2\63\7\3\2\2\2\64\65\5\24\13\2\65\67\7\b\2\2\668\5\n\6\2\67"+
		"\66\3\2\2\2\678\3\2\2\289\3\2\2\29:\7\t\2\2:;\7\n\2\2;<\5\20\t\2<=\7\7"+
		"\2\2=I\3\2\2\2>?\5\24\13\2?A\7\b\2\2@B\5\n\6\2A@\3\2\2\2AB\3\2\2\2BC\3"+
		"\2\2\2CD\7\t\2\2DE\7\n\2\2EF\7\24\2\2FG\7\7\2\2GI\3\2\2\2H\64\3\2\2\2"+
		"H>\3\2\2\2I\t\3\2\2\2JO\5\f\7\2KL\7\13\2\2LN\5\f\7\2MK\3\2\2\2NQ\3\2\2"+
		"\2OM\3\2\2\2OP\3\2\2\2P\13\3\2\2\2QO\3\2\2\2RS\5\22\n\2ST\7\n\2\2TU\5"+
		"\20\t\2U\\\3\2\2\2VW\7\f\2\2WX\5\22\n\2XY\7\n\2\2YZ\5\20\t\2Z\\\3\2\2"+
		"\2[R\3\2\2\2[V\3\2\2\2\\\r\3\2\2\2]^\7\r\2\2^_\7\16\2\2_`\5\24\13\2`a"+
		"\7\n\2\2ab\5\20\t\2bc\7\7\2\2c\17\3\2\2\2de\t\2\2\2ei\7\17\2\2fi\t\3\2"+
		"\2gi\7\27\2\2hd\3\2\2\2hf\3\2\2\2hg\3\2\2\2i\21\3\2\2\2jl\7\27\2\2km\7"+
		"\26\2\2lk\3\2\2\2lm\3\2\2\2m\23\3\2\2\2no\7\27\2\2o\25\3\2\2\2\16\30\32"+
		"#)\62\67AHO[hl";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}