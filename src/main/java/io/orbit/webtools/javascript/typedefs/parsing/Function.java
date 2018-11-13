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
    private transient Type returnType;
    private List<Parameter> parameters = new ArrayList<>();
    private transient Signature signature;

    public Function(TypeDeclaration declaration)
    {
        this.name = declaration.getName();
        this.signature = declaration.getSignatures()[0];
        for (int j = 0; j < signature.getParameters().length; j++)
            this.parameters.add(new Parameter(signature.getParameters()[j]));
    }

    public Function() { }

    public void resolve(Scope scope)
    {
        this.returnType = scope.typeOfFragment(this.signature.getType());
        this.parameters.forEach(param -> param.resolve(scope));
    }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
    public List<Parameter> getParameters() { return parameters; }
    public Type getReturnType() { return returnType; }
    public void setReturnType(Type returnType) { this.returnType = returnType; }
    public Signature getSignature() { return signature; }
    public void setSignature(Signature signature) { this.signature = signature; }
}