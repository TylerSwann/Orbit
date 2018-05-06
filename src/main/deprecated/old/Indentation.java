package io.orbit.api;

/**
 * Created by Tyler Swann on Sunday March 25, 2018 at 14:51
 */
@Deprecated
public class Indentation
{
    public final int insertAtChar;
    public final int amount;

    public static final Indentation EMPTY = new Indentation(0, 0);

    public Indentation(int insertAtChar, int amount)
    {
        this.insertAtChar = insertAtChar;
        this.amount = amount;
    }
}

