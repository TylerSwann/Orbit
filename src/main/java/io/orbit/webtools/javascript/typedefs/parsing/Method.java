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
import io.orbit.webtools.javascript.typedefs.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 02, 2018
 * Time: 3:31 PM
 * Website: https://orbiteditor.com
 */
public class Method
{
    private String name;
    private Type returnType;
    private Type owner;
    private List<Parameter> parameters = new ArrayList<>();
    private transient Signature signature;

    public Method(TypeDeclaration declaration)
    {
        this.name = declaration.getName();
        this.signature = declaration.getSignatures()[0];
        for (int j = 0; j < signature.getParameters().length; j++)
            this.parameters.add(new Parameter(signature.getParameters()[j]));
    }

    public void resolve()
    {
//        if (this.signature.getType().getName() == null)
//            System.out.println(this.name);
        this.returnType = Scope.typeWithSignature(this.signature);
        this.parameters.forEach(Parameter::resolve);
    }

    public String getName() { return name; }
    public List<Parameter> getParameters() { return parameters; }
    public Type getReturnType() { return returnType; }
    public Type getOwner() { return owner; }
}