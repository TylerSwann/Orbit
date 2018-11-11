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
import io.orbit.webtools.javascript.typedefs.parsing.Scope;

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
        if (projectScope != null)
        {
            new JavaScriptAutoCompleter(this.editor, projectScope);
            return;
        }
        loadProjectScope(() -> new JavaScriptAutoCompleter(this.editor, projectScope));
    }

    private void loadProjectScope(Runnable completion)
    {
        projectScope = new ProjectScope(LocalUser.project.getProjectRoot());
        URL lib = getClass().getClassLoader().getResource("webtools/typedefs/es5/lib.es5.json");
        assert lib != null;
        projectScope.loadLibrary(new File(lib.getFile()).getParentFile(), (library) -> completion.run());
    }

//
//    private static void loadEs5Lib()
//    {
//        Task<TypeDefinition> task = new Task<TypeDefinition>() {
//            @Override
//            protected TypeDefinition call() throws Exception
//            {
//                TypeDefinition definition = new TypeDefinition();
//                URL lib = getClass().getClassLoader().getResource("webtools/typedefs/es5/lib.es5.json");
//                assert lib != null;
//                File libDir = new File(lib.getFile()).getParentFile();
//                File[] files = libDir.listFiles();
//                assert files != null;
//                for (File file : files)
//                {
//                    try
//                    {
//                        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
//                        String json = new String(data);
//                        Gson gson = new Gson();
//                        TypeDeclaration declaration = gson.fromJson(json, TypeDeclaration.class);
//                        definition.read(declaration);
//                    }
//                    catch (IOException ex) { ex.printStackTrace(); }
//                }
//                return definition;
//            }
//        };
//        task.setOnSucceeded(event -> {
//            ES5 = task.getValue();
//            ES5.resolve();
//        });
//        task.setOnFailed(event -> System.err.println("ERROR: Failed to load ES5 lib from threaded task!"));
//        service.execute(task);
//
//    }
//
//    private static void load(String path)
//    {
//        try
//        {
//            URL url = JavaScriptController.class.getClassLoader().getResource(path);
//            assert url != null;
//            byte[] data = Files.readAllBytes(Paths.get(new File(url.getFile()).getPath()));
//            String json = new String(data);
//            Gson gson = new Gson();
//            TypeDeclaration declaration = gson.fromJson(json, TypeDeclaration.class);
//            TypeDefinition definition = new TypeDefinition();
//            definition.read(declaration);
//            definition.resolve();
//        }
//        catch (IOException ex) { ex.printStackTrace(); }
//    }
}
