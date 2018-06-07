package io.orbit.api.autocompletion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 19:57
 */
public class AutoCompletionOption extends AutoCompletionBase
{

    public AutoCompletionOption(String text, String insertedText, List<? extends AutoCompletionBase> subOptions)
    {
        this(text, insertedText);
        this.subOptions = subOptions;
    }
    public AutoCompletionOption(String text, String insertedText)
    {
        this.text = text;
        this.insertedText = insertedText;
        this.subOptions = new ArrayList<>();
    }

    public AutoCompletionOption()
    {

    }
}
