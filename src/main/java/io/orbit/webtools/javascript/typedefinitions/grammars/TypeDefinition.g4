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
 */
/**
 * Created By: Tyler Swann.
 * Date: Saturday, Oct 27, 2018
 * Time: 2:07 PM
 * Website: https://orbiteditor.com
 */

grammar TypeDefinition;

@lexer::header { package io.orbit.webtools.javascript.typedefinitions.grammars; }
@parser::header { package io.orbit.webtools.javascript.typedefinitions.grammars; }

typeDefinition: (typeInterface | declaredVar)* EOF;

typeInterface: 'interface' name ('extends' parent=name)? '{' interfaceMember* '}';
interfaceMember:
      (argument ';')                                                #PropertyType
    | functionalTypeDef                                             #FunctionType
    ;

functionalTypeDef:
      name '(' argumentList? ')' ':' type ';'                       #NonVoidFunction
    | name '(' argumentList? ')' ':' 'void' ';'                     #VoidFunction
    ;
argumentList: argument (',' argument)*;
argument:   localName ':' type                                      #StandardArgType
            | '...' localName ':' type                              #VarArgsType
            ;
declaredVar: 'declare' 'var' name ':' type ';';

type: (STRING | BOOL | NUMBER | ANY | IDENTIFIER) '[]'              #ArrayType
    | (STRING | BOOL | NUMBER | ANY)                                #PrimitiveType
    | IDENTIFIER                                                    #CustomType
    ;

localName: IDENTIFIER QUESTION_MARK?;
name: IDENTIFIER;

STRING: 'string';
BOOL: 'boolean';
NUMBER: 'number';
ANY: 'any';
VOID: 'void';
READONLY: 'readonly';


QUESTION_MARK: '?';


IDENTIFIER: [a-zA-Z_]+[a-zA-Z0-9]+;

WhiteSpaces:                    [\t\u000B\u000C\u0020\u00A0]+ -> channel(HIDDEN);
LineTerminator:                 [\r\n\u2028\u2029] -> channel(HIDDEN);