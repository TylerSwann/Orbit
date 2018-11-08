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
package io.orbit.webtools.javascript.typedefs.parsing;

import io.orbit.webtools.javascript.typedefs.fragments.TypeDeclaration;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 02, 2018
 * Time: 3:31 PM
 * Website: https://orbiteditor.com
 */
public class TypeDefinition
{
    public static final String EXTERNAL_MODULE = "External module";
    public static final String MODULE = "Module";
    public static final String CLASS = "Class";
    public static final String INTERFACE = "Interface";
    public static final String FUNCTION = "Function";
    public static final String TYPE_ALIAS = "Type alias";
    public static final String PROPERTY = "Property";
    public static final String METHOD = "Method";
    public static final String VARIABLE = "Variable";
    public static final String ARRAY = "Array";
    public static final String ARRAY_ELEMENT = "array";
    public static final String TYPE_PARAMETER = "typeParameter";
    public static final String REFLECTION = "reflection";
    public static final String INTRINSIC = "intrinsic";
    public static final String REFERENCE = "reference";
    public static final String UNION = "union";
    public static final String STRING_LITERAL = "stringLiteral";
    public static final String INTERSECTION = "intersection";

    private Map<String, Interface> interfaces = new HashMap<>();
    private Map<String, Variable> globalVariables = new HashMap<>();
    private Map<String, Class> classes = new HashMap<>();
    private Map<String, Function> functions = new HashMap<>();
    private Scope libScope;


    public TypeDefinition()
    {
        libScope = new Scope();
    }

    public void resolve()
    {
        libScope.interfaces.putAll(this.interfaces);
        libScope.globalVariables.putAll(this.globalVariables);
        libScope.resolve(this.interfaces);
        Platform.runLater(() -> {
            this.interfaces.forEach((key, anInterface) -> anInterface.resolve(this.libScope));
            this.globalVariables.forEach((key, variable) -> variable.resolve(this.libScope));
            this.functions.forEach((key, func) -> func.resolve(this.libScope));
            this.print(this.interfaces.get("Array"));
        });
    }

    public void read(TypeDeclaration declaration)
    {
        switch (declaration.getKindString())
        {
            case TypeDefinition.INTERFACE:
                this.interfaces.put(declaration.getName(), new Interface(declaration));
                break;
            case TypeDefinition.METHOD:
                break;
            case TypeDefinition.PROPERTY:
                break;
            case TypeDefinition.CLASS:
                this.classes.put(declaration.getName(), new Class(declaration));
                break;
            case TypeDefinition.FUNCTION:
                this.functions.put(declaration.getName(), new Function(declaration));
                break;
            case TypeDefinition.VARIABLE:
                this.globalVariables.put(declaration.getName(), new Variable(declaration));
                break;
            default:
                for (int i = 0; i < declaration.getChildren().length; i++)
                    read(declaration.getChildren()[i]);
                break;
        }
    }

    private void print()
    {
        this.interfaces.forEach((key, value) -> this.print(value));
        this.functions.forEach((key, value) -> this.print(value));
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

    public Map<String, Interface> getInterfaces() { return interfaces; }
    public void setInterfaces(Map<String, Interface> interfaces) { this.interfaces = interfaces; }
    public Map<String, Variable> getGlobalVariables() { return globalVariables; }
    public void setGlobalVariables(Map<String, Variable> globalVariables) { this.globalVariables = globalVariables; }
    public Scope getLibScope() { return libScope; }
    public void setLibScope(Scope libScope) { this.libScope = libScope; }
}
