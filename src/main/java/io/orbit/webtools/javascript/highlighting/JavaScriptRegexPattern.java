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
package io.orbit.webtools.javascript.highlighting;

import io.orbit.api.highlighting.HighlightType;
import io.orbit.api.highlighting.RegexStylePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 20, 2018
 * Time: 5:34 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptRegexPattern extends RegexStylePattern
{
    private static final Pattern pattern;
    private static final Map<String, HighlightType> map;

    private static final String KEYWORDS = "";

    static {
        pattern = Pattern.compile("");
        map = new HashMap<>();
    }

    public JavaScriptRegexPattern()
    {
        super(pattern, map);
    }
}
