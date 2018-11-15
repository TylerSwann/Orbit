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

import io.orbit.webtools.javascript.typedefs.fragments.ParameterFragment;
import io.orbit.webtools.javascript.typedefs.fragments.TypeFragment;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Nov 03, 2018
 * Time: 2:20 PM
 * Website: https://orbiteditor.com
 */
public class Parameter
{
    private String name;
    private TypeFragment typeFragment;
    private Type type;

    public Parameter(ParameterFragment fragment)
    {
        this.name = fragment.getName();
        this.typeFragment = fragment.getType();
    }

    public void resolve(Scope scope)
    {
        this.type = scope.typeOfFragment(this.typeFragment);
    }

    public String getName() { return name; }
    public Type getType() { return type; }
}
