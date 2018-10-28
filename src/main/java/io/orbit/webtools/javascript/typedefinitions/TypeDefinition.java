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
package io.orbit.webtools.javascript.typedefinitions;

import io.orbit.webtools.javascript.typedefinitions.grammars.TypeDefinitionBaseListener;
import io.orbit.webtools.javascript.typedefinitions.grammars.TypeDefinitionLexer;
import io.orbit.webtools.javascript.typedefinitions.grammars.TypeDefinitionParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 27, 2018
 * Time: 3:02 PM
 * Website: https://orbiteditor.com
 */
public class TypeDefinition extends TypeDefinitionBaseListener
{
    public TypeDefinition(File source) throws IOException
    {
        byte[] data = Files.readAllBytes(Paths.get(source.getPath()));
        TypeDefinitionLexer lexer = new TypeDefinitionLexer(CharStreams.fromString(new String(data)));
        TypeDefinitionParser parser = new TypeDefinitionParser(new CommonTokenStream(lexer));
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, parser.typeDefinition());
    }

    @Override public void enterPropertyType(TypeDefinitionParser.PropertyTypeContext context)
    {
//        if (context.argument() instanceof TypeDefinitionParser.StandardArgTypeContext)
//        {
//            TypeDefinitionParser.StandardArgTypeContext standardArg = (TypeDefinitionParser.StandardArgTypeContext)context.argument();
//            System.out.println(String.format("Standard %s typeIs: %s", standardArg.localName().getText(), standardArg.type().getText()));
//        }
//        else if (context.argument() instanceof TypeDefinitionParser.VarArgsTypeContext)
//        {
//            TypeDefinitionParser.VarArgsTypeContext varArg = (TypeDefinitionParser.VarArgsTypeContext)context.argument();
//            System.out.println(String.format("Standard %s typeIs: %s", varArg.localName().getText(), varArg.type().getText()));
//        }

    }

    @Override public void enterFunctionType(TypeDefinitionParser.FunctionTypeContext context)
    {

    }
//
//    @Override public void enterNonVoidFunction(TypeDefinitionParser.NonVoidFunctionContext context)
//    {
//        System.out.println(String.format("NonVoid function named: %s; Returns::%s", context.name().getText(), context.type().getText()));
//    }

//    @Override public void enterVoidFunction(TypeDefinitionParser.VoidFunctionContext context)
//    {
//        if (context.argumentList() != null)
//        {
//            System.out.println(String.format("%s:", context.name().getText()));
//            context.argumentList().argument().forEach(arg -> {
//                if (arg instanceof TypeDefinitionParser.StandardArgTypeContext)
//                {
//                    TypeDefinitionParser.StandardArgTypeContext standardArg = (TypeDefinitionParser.StandardArgTypeContext)arg;
//                    System.out.println(String.format("\t%s: %s", standardArg.localName().getText(), standardArg.type().getText()));
//                }
//                else if (arg instanceof TypeDefinitionParser.VarArgsTypeContext)
//                {
//                    TypeDefinitionParser.VarArgsTypeContext varArg = (TypeDefinitionParser.VarArgsTypeContext)arg;
//                    System.out.println(String.format("\t%s: %s", varArg.localName().getText(), varArg.type().getText()));
//                }
//            });
//        }
////        if (context.argumentList() != null)
////            System.out.println(String.format("Void function named: %s; Args::%s", context.name().getText(), context.argumentList().getText()));
////        else
////            System.out.println(String.format("Void function named: %s; NoArgs", context.name().getText()));
//    }
}