package io.orbit.webtools.css;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 19:57
 */
public class CSS3AutoCompletionOption
{
    protected String text;
    protected String insertedText;
    protected transient String insertedTextFragment;
    protected List<CSS3AutoCompletionOption> subOptions;

    public CSS3AutoCompletionOption(String text, String insertedText, List<CSS3AutoCompletionOption> subOptions)
    {
        this(text, insertedText);
        this.subOptions = subOptions;
    }
    public CSS3AutoCompletionOption(String text, String insertedText)
    {
        this.text = text;
        this.insertedText = insertedText;
        this.subOptions = new ArrayList<>();
    }

    public CSS3AutoCompletionOption() {}

    @Override
    public String toString()
    {
        return this.insertedText;
    }

    public String getText() { return this.text; }
    public String getInsertedText() { return this.insertedText; }
    public List<CSS3AutoCompletionOption> getSubOptions() { return this.subOptions; }
}
