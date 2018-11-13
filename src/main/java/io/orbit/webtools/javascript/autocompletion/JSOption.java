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

import io.orbit.webtools.javascript.typedefs.parsing.*;
import io.orbit.webtools.javascript.typedefs.parsing.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 08, 2018
 * Time: 7:39 PM
 * Website: https://orbiteditor.com
 */
public class JSOption
{
    public final String displayText;
    public final String insertionText;
    public final Type type;
    public final boolean isFunction;

    enum OptionType
    {
        VARIABLE,
        FUNCTION,
        CLASS,
        INTERFACE,
        PROPERTY
    }

    private JSOption(String displayText, String insertionText, boolean isFunction, Type type)
    {
        this.displayText = displayText;
        this.insertionText = insertionText;
        this.isFunction = isFunction;
        this.type = type;
    }

    static JSOption from(Variable variable)
    {
        return new JSOption(String.format("%s: %s", variable.getName(), variable.getType().getName()), variable.getName(), false, variable.getType());
    }

    static JSOption from(Class aClass)
    {
        return new JSOption(String.format("interface %s", aClass.getName()), aClass.getName(), false, aClass);
    }

    static JSOption from(Interface anInterface)
    {
        return new JSOption(String.format("interface %s", anInterface.getName()), anInterface.getName(), false, anInterface);
    }

    static JSOption from(Property property)
    {
        return new JSOption(String.format("%s: %s", property.getName(), property.getType().getName()), property.getName(), false, property.getType());
    }

    static JSOption from(Function function)
    {
        String displayText = displayTextWith(function.getName(), function.getParameters(), function.getReturnType());
        return new JSOption(displayText, String.format("%s()", function.getName()), true, function.getReturnType());
    }

    static <T extends Type> List<JSOption> from(T type)
    {
        return from(type.getProperties(), type.getMethods());
    }

    static List<JSOption> from(List<Property> properties, List<? extends Function> functions)
    {
        List<JSOption> options = new ArrayList<>(fromProperties(properties));
        options.addAll(fromFunctions(functions));
        return options;
    }

    static List<JSOption> fromProperties(List<Property> properties)
    {
        return properties.stream().map(JSOption::from).collect(Collectors.toList());
    }

    static List<JSOption> fromFunctions(List<? extends Function> functions)
    {
        return functions.stream().map(JSOption::from).collect(Collectors.toList());
    }

    private static String displayTextWith(String name, List<Parameter> parameters, Type returnType)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s(", name));
        parameters.forEach(param -> {
            if (parameters.indexOf(param) == parameters.size() - 1)
                builder.append(String.format("%s: %s) => %s", param.getName(), param.getType().getName(), returnType.getName()));
            else
                builder.append(String.format("%s: %s, ", param.getName(), param.getType().getName()));
        });
        if (parameters.size() <= 0)
            builder.append(String.format(") => %s", returnType.getName()));
        return builder.toString();
    }
}