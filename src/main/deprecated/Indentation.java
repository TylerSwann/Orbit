package io.orbit.api;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 13:34
 */
public class Indentation
{
    public final int tabs;
    public final String indent;

    public Indentation(int tabs)
    {
        this.tabs = tabs;
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < tabs; i++)
            builder.append("    ");
        this.indent = builder.toString();
    }
}
