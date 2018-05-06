package io.orbit.util;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 15:20
 */
public class TriSet<T, E, U>
{
    public final T first;
    public final E second;
    public final U third;

    public TriSet(T first, E second, U third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
