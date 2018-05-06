import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 12:16
 */
public interface LanguageDelegate
{
    int[] getKeywords();
    int[] getTypeRule();
    int[] getBlockCommentRule();
    int[] getLineCommentRule();
    ParserRuleContext getPrimaryExpression(String source);
    Parser getParser();
}


/*



*/