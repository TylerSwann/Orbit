grammar SampleLanguage;

@lexer::header { package io.orbit.text.antlr; }
@parser::header { package io.orbit.text.antlr; }


program: (variable | function | comment)* EOF;

function: accessModifier? FUNC name '(' argumentList? ')' ('->' returnType=type)? block;
argumentList: argument (',' argument)*;
argument: (externalName=name) (interalName=name)? ':' type ;
block: '{' (variable | ifStatement)* '}';

accessModifier: PUBLIC | PRIVATE | PROTECTED | INTERNAL;

ifStatement: 'if' '(' value conditionalOperator value ')' block;

variable: (VAR | LET) name ((':' type) | ('=' value) | ((':' type)('=' value))) ';';
name: IDENTIFIER;
value :BOOL | LONG | FLOAT | DOUBLE | INT | STRING;
type: IDENTIFIER;
conditionalOperator: '==' | '<' | '>' | '<=' | '>=' | '!=';

comment: BLOCK_COMMENT | LINE_COMMENT;

/*  Keywords  */

VAR: 'var';
LET: 'let';
FUNC: 'func';
PRIVATE: 'private';
PUBLIC: 'public';
PROTECTED: 'protected';
INTERNAL: 'internal';

/*  Primitve Types  */
STRING : ('"'~('\r' | '\n' | '"')*'"' | '\''~('\r' | '\n' | '"')*'\'') ;
BOOL: TRUE | FALSE;
LONG: ((DIGIT+ '.' DIGIT+) | (DIGIT+)) ('l' | 'L');
FLOAT: ((DIGIT+ '.' DIGIT+) | (DIGIT+)) ('f' | 'F');
DOUBLE: (DIGIT+ '.' DIGIT+) | (DIGIT+ ('d' | 'D'));
INT: DIGIT+;
fragment FALSE: 'false';
fragment TRUE: 'true';
fragment DIGIT: '0'..'9';

// Naming
IDENTIFIER: ([a-zA-Z])+ ([0-9a-zA-Z])* '_'? ([0-9a-zA-Z])*;

// COMMENTS
BLOCK_COMMENT: ('/*' .*? '*/');
LINE_COMMENT: ('//' .*? ('\n' | EOF));

// Skip whitespace
WS: [ \t\n\r]+ -> channel(HIDDEN);



