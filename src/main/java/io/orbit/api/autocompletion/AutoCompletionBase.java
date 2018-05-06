package io.orbit.api.autocompletion;

import java.util.List;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 14:28
 */
public class AutoCompletionBase
{

    protected String option;
    protected String insertedText;
    protected List<? extends AutoCompletionBase> subOptions;

    /**
     * The text preview of the unformattedOption that is displayed in
     * the AutoCompletionDialog window
     */
    public String getOption() { return this.option; }

    /**
     * The actual text that gets inserted into the document if selected
     * by the user
     */
    public String getInsertedText() { return this.insertedText; }

    /**
     * These are options that are displayed after this specific unformattedOption has been selected.
     * Example:
     *      If we were creating autocompletion for CSS, we could have an AutoCompletionBase for the
     *      background-color property. If the user selects the background-color property, we want to give them
     *      additional options that are specific to this property. So for the subOptions, we could create options
     *      for the colors red, blue, green, yellow, as well as an unformattedOption for a custom rgb color.
     */
    public List<? extends AutoCompletionBase> getSubOptions() { return this.subOptions; }

    public AutoCompletionBase(){}

    /**
     *
     * @return Boolean indicated whether or not this is a formatted template option. Ex: AutoCompletionTemplateOption
     */
    public boolean isTemplate()
    {
        return false;
    }
}
