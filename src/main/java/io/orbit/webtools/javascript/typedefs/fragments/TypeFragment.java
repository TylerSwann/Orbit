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
package io.orbit.webtools.javascript.typedefs.fragments;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 02, 2018
 * Time: 3:31 PM
 * Website: https://orbiteditor.com
 */
public class TypeFragment
{
    private String type;
    private String name;
    private ElementType elementType;
    private Declaration declaration;
    private TypeFragment[] types = new TypeFragment[0];

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public ElementType getElementType() {
        return elementType;
    }
    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }
    public void setDeclaration(Declaration declaration) { this.declaration = declaration; }
    public Declaration getDeclaration() { return this.declaration; }
    public TypeFragment[] getTypes() { return types; }
    public void setTypes(TypeFragment[] types) { this.types = types; }
}
