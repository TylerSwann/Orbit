package io.orbit.util;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/**
 * Created by Tyler Swann on Thursday February 22, 2018 at 17:49
 */
public class DoubleConverter extends StyleConverterImpl<String, Double>
{
    private static class Holder
    {
        static final DoubleConverter INSTANCE = new DoubleConverter();
    }

    public static StyleConverter<String, Double> getInstance()
    {
        return Holder.INSTANCE;
    }

    private DoubleConverter() { super(); }

    @Override
    public Double convert(ParsedValue<String, Double> value, Font not_used)
    {
        try
        {
            return Double.parseDouble(value.getValue());
        }
        catch (NumberFormatException ex) { System.err.println("io.orbit.util.DoubleConverter Error parsing Double value from String"); }
        return 0.0;
    }

    @Override
    public String toString()
    {
        return "io.orbit.util.DoubleConverter";
    }
}
