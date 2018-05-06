grammar PlainText;
@lexer::header { package io.orbit.text.antlr.plaintext; }
@parser::header { package io.orbit.text.antlr.plaintext; }









document: ANY;

ANY: [.]*;
WS: [ \t\n\r]+ -> channel(HIDDEN);







