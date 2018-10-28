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
package io.orbit.webtools.javascript;

import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.javascript.typedefinitions.TypeDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Oct 26, 2018
 * Time: 2:41 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptController implements EditorController
{
    private JavaScriptCodeFormatter formatter;
    private ArrayList<TypeDefinition> typeDefs = new ArrayList<>();

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.formatter = new JavaScriptCodeFormatter(editor);
        this.registerListeners();
        URL consoleTypeDef = getClass().getClassLoader().getResource("webtools/typedefs/console.d.ts");
        assert consoleTypeDef != null;
        try
        {
            this.typeDefs.add(new TypeDefinition(new File(consoleTypeDef.getFile())));
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void registerListeners()
    {
        this.formatter.play();
    }
}
