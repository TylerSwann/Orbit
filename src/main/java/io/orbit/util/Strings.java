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
 */
package io.orbit.util;

import javafx.scene.control.IndexRange;

/**
 * Created by Tyler Swann on Saturday August 25, 2018 at 14:13
 */
public class Strings
{
    private Strings() {}

    public static IndexRange alphanumericIndex(String text)
    {
        int start = -1;
        int end = -1;
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++)
        {
            char character = characters[i];
            if (character != ' ' && start == -1)
                start = i;
            if (character != ' ')
                end = i;
        }
        return new IndexRange(start, end);
    }
    public static String trailingSpace(String text)
    {
        IndexRange range = alphanumericIndex(text);
        return text.substring(0, range.getStart());
    }

    public static String removeTrailingSpace(String text)
    {
        IndexRange range = alphanumericIndex(text);
        return text.substring(range.getStart(), range.getEnd());
    }
}
