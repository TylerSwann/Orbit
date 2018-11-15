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

import java.util.List;


/**
 * Created By: Tyler Swann.
 * Date: Sunday, Nov 04, 2018
 * Time: 9:44 AM
 * Website: https://orbiteditor.com
 */
public class Array extends Type
{
    private Type elementType;

    Array(String name, Type elementType, List<Property> properties, List<Method> methods)
    {
        super(name, properties, methods);
        this.elementType = elementType;
    }


    public Type getElementType() { return elementType; }
    public void setElementType(Type type) { this.elementType = type; }
}