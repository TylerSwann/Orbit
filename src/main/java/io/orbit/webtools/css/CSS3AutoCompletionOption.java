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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler Swann on Saturday March 17, 2018 at 19:57
 */
public class CSS3AutoCompletionOption
{
    protected String text;
    protected String insertedText;
    protected transient String insertedTextFragment;
    protected List<CSS3AutoCompletionOption> subOptions;

    public CSS3AutoCompletionOption(String text, String insertedText, List<CSS3AutoCompletionOption> subOptions)
    {
        this(text, insertedText);
        this.subOptions = subOptions;
    }
    public CSS3AutoCompletionOption(String text, String insertedText)
    {
        this.text = text;
        this.insertedText = insertedText;
        this.subOptions = new ArrayList<>();
    }

    public CSS3AutoCompletionOption() {}

    @Override
    public String toString()
    {
        return this.insertedText;
    }

    public String getText() { return this.text; }
    public String getInsertedText() { return this.insertedText; }
    public List<CSS3AutoCompletionOption> getSubOptions() { return this.subOptions; }
}
