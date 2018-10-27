/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
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
