// Generated from C:\Users\TylersDesktop\IdeaProjects\Orbit\src\main\java\io\orbit\webtools\javascript\typedefinitions\grammars\TypeDefinition.g4 by ANTLR 4.7
 package io.orbit.webtools.javascript.typedefinitions.grammars; 
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeDefinitionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, STRING=14, BOOL=15, NUMBER=16, 
		ANY=17, VOID=18, READONLY=19, QUESTION_MARK=20, IDENTIFIER=21, WhiteSpaces=22, 
		LineTerminator=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "STRING", "BOOL", "NUMBER", "ANY", 
		"VOID", "READONLY", "QUESTION_MARK", "IDENTIFIER", "WhiteSpaces", "LineTerminator"
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


	public TypeDefinitionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TypeDefinition.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u00a3\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3"+
		"\26\6\26\u0090\n\26\r\26\16\26\u0091\3\26\6\26\u0095\n\26\r\26\16\26\u0096"+
		"\3\27\6\27\u009a\n\27\r\27\16\27\u009b\3\27\3\27\3\30\3\30\3\30\3\30\2"+
		"\2\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\3\2\6\5\2C\\aac|\5\2\62;C\\"+
		"c|\6\2\13\13\r\16\"\"\u00a2\u00a2\5\2\f\f\17\17\u202a\u202b\2\u00a5\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2"+
		"\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2"+
		"\3\61\3\2\2\2\5;\3\2\2\2\7C\3\2\2\2\tE\3\2\2\2\13G\3\2\2\2\rI\3\2\2\2"+
		"\17K\3\2\2\2\21M\3\2\2\2\23O\3\2\2\2\25Q\3\2\2\2\27U\3\2\2\2\31]\3\2\2"+
		"\2\33a\3\2\2\2\35d\3\2\2\2\37k\3\2\2\2!s\3\2\2\2#z\3\2\2\2%~\3\2\2\2\'"+
		"\u0083\3\2\2\2)\u008c\3\2\2\2+\u008f\3\2\2\2-\u0099\3\2\2\2/\u009f\3\2"+
		"\2\2\61\62\7k\2\2\62\63\7p\2\2\63\64\7v\2\2\64\65\7g\2\2\65\66\7t\2\2"+
		"\66\67\7h\2\2\678\7c\2\289\7e\2\29:\7g\2\2:\4\3\2\2\2;<\7g\2\2<=\7z\2"+
		"\2=>\7v\2\2>?\7g\2\2?@\7p\2\2@A\7f\2\2AB\7u\2\2B\6\3\2\2\2CD\7}\2\2D\b"+
		"\3\2\2\2EF\7\177\2\2F\n\3\2\2\2GH\7=\2\2H\f\3\2\2\2IJ\7*\2\2J\16\3\2\2"+
		"\2KL\7+\2\2L\20\3\2\2\2MN\7<\2\2N\22\3\2\2\2OP\7.\2\2P\24\3\2\2\2QR\7"+
		"\60\2\2RS\7\60\2\2ST\7\60\2\2T\26\3\2\2\2UV\7f\2\2VW\7g\2\2WX\7e\2\2X"+
		"Y\7n\2\2YZ\7c\2\2Z[\7t\2\2[\\\7g\2\2\\\30\3\2\2\2]^\7x\2\2^_\7c\2\2_`"+
		"\7t\2\2`\32\3\2\2\2ab\7]\2\2bc\7_\2\2c\34\3\2\2\2de\7u\2\2ef\7v\2\2fg"+
		"\7t\2\2gh\7k\2\2hi\7p\2\2ij\7i\2\2j\36\3\2\2\2kl\7d\2\2lm\7q\2\2mn\7q"+
		"\2\2no\7n\2\2op\7g\2\2pq\7c\2\2qr\7p\2\2r \3\2\2\2st\7p\2\2tu\7w\2\2u"+
		"v\7o\2\2vw\7d\2\2wx\7g\2\2xy\7t\2\2y\"\3\2\2\2z{\7c\2\2{|\7p\2\2|}\7{"+
		"\2\2}$\3\2\2\2~\177\7x\2\2\177\u0080\7q\2\2\u0080\u0081\7k\2\2\u0081\u0082"+
		"\7f\2\2\u0082&\3\2\2\2\u0083\u0084\7t\2\2\u0084\u0085\7g\2\2\u0085\u0086"+
		"\7c\2\2\u0086\u0087\7f\2\2\u0087\u0088\7q\2\2\u0088\u0089\7p\2\2\u0089"+
		"\u008a\7n\2\2\u008a\u008b\7{\2\2\u008b(\3\2\2\2\u008c\u008d\7A\2\2\u008d"+
		"*\3\2\2\2\u008e\u0090\t\2\2\2\u008f\u008e\3\2\2\2\u0090\u0091\3\2\2\2"+
		"\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094\3\2\2\2\u0093\u0095"+
		"\t\3\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0097,\3\2\2\2\u0098\u009a\t\4\2\2\u0099\u0098\3\2\2\2"+
		"\u009a\u009b\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d"+
		"\3\2\2\2\u009d\u009e\b\27\2\2\u009e.\3\2\2\2\u009f\u00a0\t\5\2\2\u00a0"+
		"\u00a1\3\2\2\2\u00a1\u00a2\b\30\2\2\u00a2\60\3\2\2\2\6\2\u0091\u0096\u009b"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}