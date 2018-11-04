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

import io.orbit.webtools.javascript.typedefs.Signature;
import io.orbit.webtools.javascript.typedefs.TypeFragment;
import javafx.application.Platform;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Nov 03, 2018
 * Time: 4:21 PM
 * Website: https://orbiteditor.com
 */
public class Scope
{
    public static final Map<String, Interface> interfaces = new HashMap<>();
    public static final Map<String, Variable> globalVariables = new HashMap<>();
    private static ArrayFactory ARRAY_FACTORY;


    public static void resolve(Map<String, Interface> interfaces)
    {
        Interface arrayInterface = interfaces.get(TypeDefinition.ARRAY);
        ARRAY_FACTORY = new ArrayFactory(arrayInterface.getMethods(), arrayInterface.getProperties());
    }

    public static Type typeWithName(String name)
    {
        boolean isPrimitive = isPrimitive(name);
        if (name != null)
        {
            for (String key : interfaces.keySet())
            {
                Interface anInterface = interfaces.get(key);
                if (isPrimitive && anInterface.getName().toLowerCase().equals(name.toLowerCase()))
                    return anInterface;
                if (anInterface.getName().equals(name))
                    return anInterface;
            }
        }
        if (Type.VOID.getName().equals(name))
            return Type.VOID;
        else if (Type.THIS.getName().equals(name))
            return Type.THIS;
        else if (Type.ANY.getName().equals(name))
            return Type.ANY;
        return Type.NULL;
    }

    public static Type typeWithSignature(Signature signature)
    {
        return typeOfFragment(signature.getType());
    }

    public static Type typeOfFragment(TypeFragment fragment)
    {
        switch (fragment.getType())
        {
            case TypeDefinition.UNION:
                StringBuilder builder = new StringBuilder("(");
                List<TypeFragment> types = Arrays.asList(fragment.getTypes());
                types.forEach(type -> {
                    if (types.indexOf(type) == types.size() - 1)
                        builder.append(String.format("%s)", type.getName()));
                    else
                        builder.append(String.format("%s | ", type.getName()));
                });
                return new Type(builder.toString(), Collections.emptyList(), Collections.emptyList());
            case TypeDefinition.REFLECTION:
                Signature signature = fragment.getDeclaration().getSignatures()[0];
                List<Parameter> parameters = Arrays.stream(signature.getParameters()).map(Parameter::new).collect(Collectors.toList());
                parameters.forEach(Parameter::resolve);
                return new FunctionalType(typeWithSignature(signature), parameters);
            case TypeDefinition.TYPE_PARAMETER:
                return new Type(fragment.getName(), Collections.emptyList(), Collections.emptyList());
            case TypeDefinition.ARRAY_ELEMENT:
                if (TypeDefinition.TYPE_PARAMETER.equals(fragment.getElementType().getType()))
                    return ARRAY_FACTORY.arrayOf(new Type(fragment.getElementType().getName(), Collections.emptyList(), Collections.emptyList()));
                return ARRAY_FACTORY.arrayOf(typeWithName(fragment.getElementType().getName()));
            default: break;
        }
        return typeWithName(fragment.getName());
    }

    private static boolean isPrimitive(String typeName)
    {
        if (typeName == null)
            return false;
        return typeName.equals("string") || typeName.equals("number") || typeName.equals("boolean") || typeName.equals("any");
    }

    private Scope(){}
}