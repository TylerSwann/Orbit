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
package io.orbit.webtools;

import io.orbit.api.text.CodeEditor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by Tyler Swann on Saturday June 23, 2018 at 19:04
 */
public abstract class CodeFormatter
{
    public abstract String reformat(String source);
    protected final CodeEditor editor;
    protected SimpleObjectProperty<Boolean> paused = new SimpleObjectProperty<>(true);
    public void pause() { this.paused.set(true); }
    public void play() {  this.paused.set(false);  }
    public ObservableValue<Boolean> pausedProperty() {  return paused;  }
    public boolean isPaused() {  return this.paused.get();  }

    public CodeFormatter(CodeEditor editor)
    {
        this.editor = editor;
    }
    private CodeFormatter()
    {
        this.editor = null;
    }

    protected String getIndent(int level)
    {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < level; i++)
            builder.append("    ");
        return builder.toString();
    }
}
