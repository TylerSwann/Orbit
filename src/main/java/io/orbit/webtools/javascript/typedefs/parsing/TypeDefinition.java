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

    private TypeDefinition() { }
}
