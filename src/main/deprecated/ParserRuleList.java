package io.orbit.text;

import io.orbit.util.IndexRange;
import javafx.application.Platform;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyler Swann on Thursday March 01, 2018 at 14:55
 */
public class ParserRuleList
{
    private Map<IndexRange, ParserRuleContext> indexMap = new HashMap<>();
    private final List<Class<? extends ParserRuleContext>> targets;

    public ParserRuleList()
    {
        this.targets = new ArrayList<>();
    }
    public ParserRuleList(List<Class<? extends ParserRuleContext>> targets)
    {
        this.targets = targets;
    }
    public void map(ParserRuleContext primaryExpression)
    {
        if (isTargetRule(primaryExpression))
            addRule(primaryExpression);
        for (ParseTree child : primaryExpression.children)
            this.map(child);
    }

    public ParserRuleContext ruleAtLine(int line)
    {
        IndexRange indexRange = indexRangeAtLine(line);
        if (indexRange != null)
            return indexMap.get(indexRange);
        return null;
    }

    public boolean hasRuleAtLine(int line)
    {
        return ruleAtLine(line) != null;
    }

    public int parentTargetCountAtLine(int line)
    {
        if (!hasRuleAtLine(line))
            return 0;
        ParserRuleContext context = ruleAtLine(line);
       // Platform.runLater(this::printRanges);
        return parentTargetCount(context);
    }

    public int size() {  return this.indexMap.size();  }
    private int parentTargetCount(ParserRuleContext child)
    {
        int count = 0;
        ParserRuleContext parent = child.getParent();
        while (parent != null)
        {
            if (indexMap.containsValue(parent) || childIsTargetRule(parent, child))
                count++;
            parent = parent.getParent();
        }
        return count;
    }

    private void map(ParseTree childTree)
    {
        //System.out.println(childTree instanceof SampleLanguageParser.BlockContext);
        if (!(childTree instanceof ParserRuleContext))
            return;
        ParserRuleContext child = ((ParserRuleContext) childTree);
        if (isTargetRule(child))
            addRule(child);
        if (child.children != null)
            for (ParseTree subChild : child.children)
                map(subChild);
    }

    private void addRule(ParserRuleContext rule)
    {
        this.indexMap.put(new IndexRange(rule.start.getLine() - 1, rule.stop.getLine() - 1), rule);
    }

    private IndexRange indexRangeAtLine(int line)
    {
        for (IndexRange range : this.indexMap.keySet())
            if (range.isInRange(line))
                return range;
        return null;
    }

    private void printRanges()
    {
        for (IndexRange range : this.indexMap.keySet())
            System.out.println(range.toString());
    }

    private boolean isTargetRule(ParserRuleContext context)
    {
        for (Class<? extends ParserRuleContext> target : this.targets)
            if (target.isInstance(context))
                return true;
        return false;
    }
    private boolean childIsTargetRule(ParserRuleContext parent, ParserRuleContext ignore)
    {
        for (ParseTree child : parent.children)
            if (child instanceof ParserRuleContext && child != ignore && isTargetRule(((ParserRuleContext)child)))
                return true;
        return false;
    }
}
