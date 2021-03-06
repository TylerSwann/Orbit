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

import io.orbit.webtools.javascript.typedefs.fragments.Signature;
import io.orbit.webtools.javascript.typedefs.fragments.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Tyler Swann.
 * Date: Wednesday, Nov 07, 2018
 * Time: 2:53 PM
 * Website: https://orbiteditor.com
 */
public class Function
{
    private String name;
    private Type returnType;
    private List<Parameter> parameters = new ArrayList<>();
    private Signature signature;
    private int priority = 0;
    private boolean isInherited;

    public Function(TypeDeclaration declaration)
    {
        this.name = declaration.getName();
        this.signature = declaration.getSignatures()[0];
        for (int j = 0; j < signature.getParameters().length; j++)
            this.parameters.add(new Parameter(signature.getParameters()[j]));
        this.isInherited = declaration.getInheritedFrom() != null;
    }

    public void resolve(Scope scope)
    {
        this.returnType = scope.typeOfFragment(this.signature.getType());
        this.parameters.forEach(param -> param.resolve(scope));
    }

    public String getName() { return name; }
    public Type getReturnType() { return returnType; }
    public List<Parameter> getParameters() { return parameters; }
    public Signature getSignature() { return signature; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isInherited() { return isInherited; }
}