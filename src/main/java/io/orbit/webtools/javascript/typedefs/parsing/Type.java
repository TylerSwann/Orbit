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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Nov 03, 2018
 * Time: 2:02 PM
 * Website: https://orbiteditor.com
 */
public class Type
{
    public static transient final Type VOID = new Type("void", Collections.emptyList(), Collections.emptyList());
    public static transient final Type NULL = new Type("null", Collections.emptyList(), Collections.emptyList());
    public static transient final Type THIS = new Type("this", Collections.emptyList(), Collections.emptyList());
    public static transient final Type ANY = new Type("any", Collections.emptyList(), Collections.emptyList());

    private List<Property> properties;
    private List<Method> methods;
    private String name;
    private boolean isArray = false;

    public Type(String name, List<Property> properties, List<Method> methods)
    {
        this.name = name;
        this.properties = properties;
        this.methods = methods;
    }

    public Type(String name, boolean isArray, List<Property> properties, List<Method> methods)
    {
        this.name = name;
        this.properties = properties;
        this.methods = methods;
        this.isArray = isArray;
    }

    protected Type(TypeDeclaration declaration)
    {
        List<Property> properties = new ArrayList<>();
        List<Method> methods = new ArrayList<>();
        for (int i = 0; i < declaration.getChildren().length; i++)
        {
            TypeDeclaration child = declaration.getChildren()[i];
            switch (child.getKindString())
            {
                case TypeDefinition.PROPERTY:
                    properties.add(new Property(child));
                    break;
                case TypeDefinition.METHOD:
                    methods.add(new Method(child));
                    break;
                case TypeDefinition.INTERFACE:
                case TypeDefinition.VARIABLE:
                    System.out.println(String.format("Interface has interface or variable named %s", child.getName()));
                    break;
                default:
                    break;
            }
        }
        this.methods = methods;
        this.properties = properties;
        this.name = declaration.getName();
    }

    public List<Property> getProperties() { return properties; }
    public List<Method> getMethods() { return methods; }
    public String getName() { return name; }
    protected void setName(String name) { this.name = name; }
    public boolean isArray() { return isArray; }
}