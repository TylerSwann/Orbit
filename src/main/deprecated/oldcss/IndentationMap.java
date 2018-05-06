package io.orbit.text;

import io.orbit.api.CharacterPair;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyledDocument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Thursday March 01, 2018 at 19:01
 */
@Deprecated
public class IndentationMap
{
    private final Map<Integer, Integer> indexMap;
    private CharacterPair hierarchicalPair;

    public IndentationMap(CharacterPair hierarchicalPair)
    {
        this.indexMap = new HashMap<>();
        this.hierarchicalPair = hierarchicalPair;
    }

    public void compute(StyledDocument<Collection<String>, String, Collection<String>> document)
    {
        this.indexMap.clear();
        int indentationLevel = 0;
        for (int i = 0; i < document.getParagraphs().size(); i++)
        {
            Paragraph<Collection<String>, String, Collection<String>> paragraph = document.getParagraph(i);
            for (int j = 0; j < paragraph.length(); j++)
            {
                char character = paragraph.charAt(j);
                if (character == hierarchicalPair.leftChar)
                {
                    indentationLevel++;
                    break;
                }
                else if (character == hierarchicalPair.rightChar)
                {
                    indentationLevel--;
                    break;
                }
            }
            this.indexMap.put(i, indentationLevel);
        }
    }

    public int indentLevelForLine(int line)
    {
        Integer level = this.indexMap.get(line);
        if (level != null)
            return level;
        return 0;
    }
}
