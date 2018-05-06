package io.orbit.util;

/**
 * Created by Tyler Swann on Friday January 12, 2018 at 14:09
 */
public class Tuple<T, R>
{
    public final T first;
    public final R second;

    public Tuple(T first, R second)
    {
        this.first = first;
        this.second = second;
    }
}
