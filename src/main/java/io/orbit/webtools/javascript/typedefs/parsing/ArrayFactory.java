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
 * Time: 1:55 PM
 * Website: https://orbiteditor.com
 */
public class ArrayFactory
{
    private List<Method> methods;
    private List<Property> properties;

    public ArrayFactory(List<Method> methods, List<Property> properties)
    {
        this.methods = methods;
        this.properties = properties;
    }

    public Array arrayOf(Type type)
    {
        return new Array(String.format("%s[]", type.getName()), type, this.properties, this.methods);
    }
}