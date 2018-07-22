package io.orbit.util;

/**
 * Created by Tyler Swann on Sunday February 25, 2018 at 17:49
 */
@Deprecated
public class IndexRange
{
    public final int start;
    public final int end;
    public final boolean isBothNegative;
    public final boolean isNegative;

    public IndexRange(int start, int end)
    {
        this.start = start;
        this.end = end;
        this.isBothNegative = this.start < 0 && this.end < 0;
        this.isNegative = this.start < 0 || this.end < 0;
    }
    public boolean isInRange(IndexRange range)
    {
        return range.start >= this.start && range.end <= this.end;
    }
    public boolean isInRange(IndexRange[] ranges)
    {
        for (IndexRange range : ranges)
            if (this.isInRange(range))
                return true;
        return false;
    }
    public boolean isInRange(int index)
    {
        return index >= this.start && index <= this.end;
    }


    @Override
    public String toString()
    {
        return String.format("%d -> %d", this.start, this.end);
    }
}
