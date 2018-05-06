import io.orbit.text.antlr.SampleLanguageLexer;
import io.orbit.text.antlr.SampleLanguageParser;
import org.antlr.v4.runtime.*;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 12:22
 */
public class SampleLanguage implements LanguageDelegate
{
    private static SampleLanguageLexer lexer;
    private static SampleLanguageParser parser;
    static {
        CodePointCharStream inputStream = CharStreams.fromString("");
        lexer = new SampleLanguageLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new SampleLanguageParser(tokens);
        parser.removeErrorListeners();
        parser.removeParseListeners();
    }

    @Override
    public ParserRuleContext getPrimaryExpression(String source)
    {
        CodePointCharStream inputStream = CharStreams.fromString(source);
        lexer.setInputStream(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser.setTokenStream(tokens);
        return parser.program();
    }
    @Override
    public Parser getParser()
    {
        return parser;
    }

    @Override
    public int[] getKeywords() {
        return new int[] {
                SampleLanguageParser.LET,
                SampleLanguageParser.VAR,
                SampleLanguageParser.BOOL,
                SampleLanguageParser.FUNC,
                SampleLanguageParser.PUBLIC,
                SampleLanguageParser.PRIVATE,
                SampleLanguageParser.PROTECTED,
                SampleLanguageParser.INTERNAL
        };
    }

    @Override
    public int[] getTypeRule() {
        return new int[] {
                SampleLanguageParser.RULE_type
        };
    }

    @Override
    public int[] getBlockCommentRule() {
        return new int[] {
                SampleLanguageParser.BLOCK_COMMENT
        };
    }

    @Override
    public int[] getLineCommentRule() {
        return new int[] {
                SampleLanguageParser.LINE_COMMENT
        };
    }
}
