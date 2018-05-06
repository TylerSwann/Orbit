import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by Tyler Swann on Saturday January 27, 2018 at 14:13
 */
public class SampleLanguageListener implements ParseTreeListener
{
    public void visitTerminal(TerminalNode node)
    {
        node.getSymbol().getType();
    }
    public void visitErrorNode(ErrorNode node){}
    public void enterEveryRule(ParserRuleContext ctx)
    {
        ctx.getRuleIndex();
    }
    public void exitEveryRule(ParserRuleContext ctx){}
}
