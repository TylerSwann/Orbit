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

import com.google.gson.Gson;
import io.orbit.api.EditorController;
import io.orbit.api.text.CodeEditor;
import io.orbit.webtools.javascript.typedefs.ParameterFragment;
import io.orbit.webtools.javascript.typedefs.TypeDeclaration;
import io.orbit.webtools.javascript.typedefs.parsing.TypeDefinition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Oct 26, 2018
 * Time: 2:41 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptController implements EditorController
{
    private JavaScriptCodeFormatter formatter;

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.formatter = new JavaScriptCodeFormatter(editor);
        try
        {
            URL url = getClass().getClassLoader().getResource("webtools/typedefs/primitives.json");
            assert url != null;
            byte[] data = Files.readAllBytes(Paths.get(new File(url.getFile()).getPath()));
            String json = new String(data);
            Gson gson = new Gson();
            TypeDeclaration def = gson.fromJson(json, TypeDeclaration.class);
            TypeDefinition definition = new TypeDefinition(def);
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    private void registerListeners()
    {
        this.formatter.play();
    }
}
