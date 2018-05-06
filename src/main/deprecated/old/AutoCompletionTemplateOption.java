package io.orbit.api;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 14:11
 */
@Deprecated
public class AutoCompletionTemplateOption extends AutoCompletionBase
{
    private final String unformattedOption;
    private final String[] placeholders;

    public AutoCompletionTemplateOption(String option, String... placeholders)
    {
        this(option, new ArrayList<>(), placeholders);

    }

    public AutoCompletionTemplateOption(String option, ArrayList<? extends AutoCompletionBase> subOptions,  String... placeholders)
    {
        this.unformattedOption = option;
        this.placeholders = placeholders;
        this.subOptions = subOptions;
        Pattern pattern = Pattern.compile("%x");
        Matcher matcher = pattern.matcher(option);
        int placeholderCount = 0;
        while (matcher.find())
            placeholderCount++;
        if (placeholderCount != placeholders.length)
        {
            String errorMsg = "Length of argument 'placeholders' in io.orbit.api.AutoCompletionTemplateOption " +
                    "must be equal to the amount of delimiters, '%x', that are in argument 'unformattedOption'!";
            throw new IllegalArgumentException(errorMsg);
        }
        String formattedOption = option;
        for (String placeholderText : placeholders)
            formattedOption = formattedOption.replaceFirst("%x", placeholderText);
        this.insertedText = formattedOption;
        this.option = formattedOption;
    }

    @Override
    public boolean isTemplate() { return true;  }

    public String getUnformattedOption() { return unformattedOption; }
    public String[] getPlaceholders() { return placeholders; }
}
