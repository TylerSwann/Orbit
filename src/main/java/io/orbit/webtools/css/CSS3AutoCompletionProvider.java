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
package io.orbit.webtools.css;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Sunday March 18, 2018 at 13:31
 */
public class CSS3AutoCompletionProvider
{
    private static CSS3AutoCompletionOption[] CSS_PROPERTIES;

    public CSS3AutoCompletionProvider()
    {
        if (CSS_PROPERTIES == null)
        {
            try
            {
                Gson gson = new Gson();
                CSS_PROPERTIES = new CSS3AutoCompletionOption[0];
                URL cssPropertiesURL = getClass().getClassLoader().getResource("webtools/css_auto_completion_options.json");
                if (cssPropertiesURL == null)
                    throw new RuntimeException("URL to resource file webtools/css_auto_completion_options.json was null in CSS3AutoCompletionProvider");
                File file = new File(cssPropertiesURL.getFile());
                JsonReader reader = new JsonReader(new FileReader(file));
                CSS_PROPERTIES = gson.fromJson(reader, CSS3AutoCompletionOption[].class);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                throw new RuntimeException("Couldn't load CSS Autocompletion properties from resource file...");
            }
        }
    }

    public List<CSS3AutoCompletionOption> optionsForLine(String currentLine)
    {
        List<CSS3AutoCompletionOption> options = new ArrayList<>();
        if (currentLine == null)
            return options;
        for (CSS3AutoCompletionOption option : CSS_PROPERTIES)
        {
            if (option.text.length() < currentLine.length())
                continue;
            currentLine = currentLine.replaceAll("^\\s*", "");
            String splitOption = option.text.substring(0, currentLine.length());

            if (splitOption.equals(currentLine))
                options.add(option);
        }
        return options;
    }

    public List<CSS3AutoCompletionOption> subOptionsForOption(CSS3AutoCompletionOption option, String currentLine)
    {
        List<CSS3AutoCompletionOption> options = new ArrayList<>();
        if (currentLine == null)
            return option.subOptions;
        for (CSS3AutoCompletionOption subOption : option.subOptions)
        {
            String line = currentLine.replaceAll(String.format("^\\s*%s", option.insertedText), "");
            if (subOption.text.length() < line.length())
                continue;
            String splitOption = subOption.text.substring(0, line.length());
            if (splitOption.equals(line))
            {
                subOption.insertedTextFragment = subOption.insertedText.replaceFirst(line, "");
                options.add(subOption);
            }
        }
        return options;
    }
}
