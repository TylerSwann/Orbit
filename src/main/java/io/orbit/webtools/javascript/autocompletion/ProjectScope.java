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
package io.orbit.webtools.javascript.autocompletion;

import com.google.gson.Gson;
import io.orbit.controllers.LanguageService;
import io.orbit.webtools.javascript.typedefs.fragments.TypeDeclaration;
import io.orbit.webtools.javascript.typedefs.parsing.*;
import io.orbit.webtools.javascript.typedefs.parsing.Class;
import javafx.concurrent.Task;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 09, 2018
 * Time: 3:50 PM
 * Website: https://orbiteditor.com
 */
public class ProjectScope
{
    private static final String NODE_MODULES = "node_modules";
    public final Scope library;
    private boolean isResolved = false;
    public boolean isResolved() { return isResolved; }
    private List<Runnable> onResolveHandlers = new ArrayList<>();
    public void addOnResolveHandler(Runnable runnable) { this.onResolveHandlers.add(runnable); }

    public ProjectScope(File root)
    {
        this.library = new Scope();
    }

    public void loadLibrary(File lib, Consumer<Scope> completion)
    {
        this.readDirectoryAsync(lib, this.library, () -> {
            this.library.resolve();
            completion.accept(this.library);
            isResolved = true;
            this.onResolveHandlers.forEach(Runnable::run);
        });
    }

    private void readDirectoryAsync(File path, Scope scope, Runnable completion)
    {
        File [] files = path.listFiles();
        if (!path.isDirectory() || files == null)
            throw new RuntimeException("Library provided to ProjectScope in readDirectoryAsync must be a directory containing files!");
        Task<Integer> readTask = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception
            {
                for (File file : files)
                {
                    TypeDeclaration declaration = read(file);
                    read(declaration, scope);
                }
                completion.run();
                return 0;
            }
        };
        LanguageService.execute(readTask);
    }

    private TypeDeclaration read(File path) throws IOException
    {
        byte[] data = Files.readAllBytes(Paths.get(path.getPath()));
        String json = new String(data);
        Gson gson = new Gson();
        return gson.fromJson(json, TypeDeclaration.class);
    }

    private void read(TypeDeclaration declaration, Scope scope)
    {
        switch (declaration.getKindString())
        {
            case TypeDefinition.INTERFACE:
                scope.interfaces.put(declaration.getName(), new Interface(declaration));
                break;
            case TypeDefinition.METHOD:
                break;
            case TypeDefinition.PROPERTY:
                break;
            case TypeDefinition.CLASS:
                scope.classes.put(declaration.getName(), new Class(declaration));
                break;
            case TypeDefinition.FUNCTION:
                scope.functions.put(declaration.getName(), new Function(declaration));
                break;
            case TypeDefinition.VARIABLE:
                scope.variables.put(declaration.getName(), new Variable(declaration));
                break;
            default:
                for (int i = 0; i < declaration.getChildren().length; i++)
                    read(declaration.getChildren()[i], scope);
                break;
        }
    }

    private void print(Interface interfce)
    {
        System.out.println(String.format("Interface %s", interfce.getName()));
        interfce.getMethods().forEach(method -> {
            System.out.print(String.format("\tMethod %s(", method.getName()));
            method.getParameters().forEach(param -> {
                if (method.getParameters().indexOf(param) == method.getParameters().size() - 1)
                    System.out.print(String.format("%s: %s) => %s\n", param.getName(), param.getType().getName(), method.getReturnType().getName()));
                else
                    System.out.print(String.format("%s: %s, ", param.getName(), param.getType().getName()));
            });
            if (method.getParameters().size() <= 0)
                System.out.print(String.format(") => %s\n", method.getReturnType().getName()));
        });
        interfce.getProperties().forEach(prop -> System.out.println(String.format("\tProperty %s: %s", prop.getName(), prop.getType().getName())));
    }

    private void print(Function function)
    {
        System.out.print(String.format("Function %s(", function.getName()));
        function.getParameters().forEach(param -> {
            if (function.getParameters().indexOf(param) == function.getParameters().size() - 1)
            {
                System.out.print(String.format("%s: %s) => %s",
                        param.getName(),
                        param.getType().getName(),
                        function.getReturnType().getName()));
            }
            else
            {
                System.out.print(String.format("%s: %s, ",
                        param.getName(),
                        param.getType().getName()));
            }
        });
    }

    private File findFolderNamed(String name, File folder)
    {
        File[] files = folder.listFiles();
        if (files == null)
            return null;
        for (File file : files)
        {
            if (file.isDirectory() && file.getName().equals(name))
                return file;
            else if (file.isDirectory())
            {
                File targetFolder = findFolderNamed(name, file);
                if (targetFolder != null)
                    return targetFolder;
            }
        }
        return null;
    }
}