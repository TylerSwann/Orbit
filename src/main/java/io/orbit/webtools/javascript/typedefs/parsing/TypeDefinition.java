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

import io.orbit.webtools.javascript.typedefs.TypeDeclaration;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 02, 2018
 * Time: 3:31 PM
 * Website: https://orbiteditor.com
 */
public class TypeDefinition
{
    public static final String INTERFACE = "Interface";
    public static final String PROPERTY = "Property";
    public static final String METHOD = "Method";
    public static final String VARIABLE = "Variable";
    public static final String ARRAY = "Array";
    public static final String ARRAY_ELEMENT = "array";
    public static final String TYPE_PARAMETER = "typeParameter";
    public static final String REFLECTION = "reflection";
    public static final String INTRINSIC = "intrinsic";
    public static final String UNION = "union";

    private Map<String, Interface> interfaces = new HashMap<>();
    private Map<String, Variable> globalVariables = new HashMap<>();

    public TypeDefinition(TypeDeclaration declaration)
    {
        read(declaration);
        Platform.runLater(() -> {
            Scope.interfaces.putAll(this.interfaces);
            Scope.globalVariables.putAll(this.globalVariables);
            Scope.resolve(this.interfaces);
            Platform.runLater(() -> {
                this.interfaces.forEach((key, anInterface) -> anInterface.resolve());
                this.globalVariables.forEach((key, variable) -> variable.resolve());
                this.print();
            });
        });
    }

    private void read(TypeDeclaration declaration)
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
        this.interfaces.forEach((key, _interface) -> {
            System.out.println(String.format("Interface %s", _interface.getName()));
            _interface.getMethods().forEach(method -> {
                System.out.print(String.format("\tmethod %s(", method.getName()));
                method.getParameters().forEach(param -> {
                    System.out.print(String.format("%s: %s, ", param.getName(), param.getType().getName()));
                });
                System.out.print(String.format("):%s\n", method.getReturnType().getName()));
            });
            _interface.getProperties().forEach(prop -> {
                System.out.println(String.format("\t%s: %s", prop.getName(), prop.getTypeName()));
            });
        });
    }
}
