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
 * Time: 6:16 PM
 * Website: https://orbiteditor.com
 */
public class FunctionalType extends Type
{
    private List<Parameter> parameters;
    private Type returnType;

    public FunctionalType(Type returnType, List<Parameter> parameters)
    {
        super("Function", returnType.getProperties(), returnType.getMethods());
        this.returnType = returnType;
        this.parameters = parameters;
        StringBuilder builder = new StringBuilder();
        builder.append("((");
        parameters.forEach(param -> {
            if (parameters.indexOf(param) == parameters.size() - 1)
                builder.append(String.format("%s: %s) => %s)", param.getName(), param.getType().getName(), returnType.getName()));
            else
                builder.append(String.format("%s: %s, ", param.getName(), param.getType().getName()));
        });
        if (parameters.size() <= 0)
            builder.append(String.format(") => %s)", returnType.getName()));
        this.setName(builder.toString());
    }

    public FunctionalType() { }

    public List<Parameter> getParameters() { return parameters; }
    public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
    public Type getReturnType() { return returnType; }
    public void setReturnType(Type returnType) { this.returnType = returnType; }
}