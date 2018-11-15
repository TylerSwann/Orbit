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
import io.orbit.settings.LocalUser;
import io.orbit.webtools.javascript.autocompletion.JavaScriptAutoCompleter;
import io.orbit.webtools.javascript.autocompletion.ProjectScope;
import java.io.File;
import java.net.URL;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Oct 26, 2018
 * Time: 2:41 PM
 * Website: https://orbiteditor.com
 */
public class JavaScriptController implements EditorController
{
    private CodeEditor editor;
    private static ProjectScope projectScope;

    @Override
    public void start(File file, CodeEditor editor)
    {
        this.editor = editor;
        JavaScriptCodeFormatter formatter = new JavaScriptCodeFormatter(editor);
        formatter.play();
        if (projectScope != null && projectScope.isResolved())
            new JavaScriptAutoCompleter(this.editor, projectScope);
        else if (projectScope != null && !projectScope.isResolved())
            projectScope.addOnResolveHandler(() -> new JavaScriptAutoCompleter(this.editor, projectScope));
        else
            loadProjectScope(() -> new JavaScriptAutoCompleter(this.editor, projectScope));
    }

    private void loadProjectScope(Runnable completion)
    {
        projectScope = new ProjectScope(LocalUser.project.getProjectRoot());
        URL lib = getClass().getClassLoader().getResource("webtools/typedefs/es5/lib.es5.json");
        assert lib != null;
        File libFile = new File(lib.getFile()).getParentFile();
        projectScope.loadLibrary(libFile, (library) -> completion.run());
    }
}
