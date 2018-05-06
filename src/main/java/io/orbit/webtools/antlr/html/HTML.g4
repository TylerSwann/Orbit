

grammar HTML;


@lexer::header {  package io.orbit.webtools.antlr.html;  }
@parser::header {  package io.orbit.webtools.antlr.html;  }

document: htmlElement* EOF;




htmlElement: voidElement | element | selfClosingElement | doctype;
//voidElement: openTag;
voidElement: '<' voidTagName attribute* '>';
element: openTag content closeTag;
selfClosingElement: '<' TAG_NAME attribute* '/>';
doctype: '<!DOCTYPE' attribute* '>';

openTag: '<' TAG_NAME attribute* '>';
closeTag: '</' TAG_NAME attribute* '>';
voidTagName: 'area' | 'base' | 'br' | 'col' | 'command' | 'embed' | 'hr' | 'img' | 'input' | 'keygen' | 'link' | 'menuitem' | 'meta' | 'param' | 'source' | 'track' | 'wbr';

//content: (htmlElement | comment)*;
content: (htmlElement | xhtmlCDATA | comment | TAG_NAME)*;
//htmlChardata: SEA_WS;
xhtmlCDATA: CDATA;

attribute: TAG_NAME ('=' STRING)?;
comment: BLOCK_COMMENT;
//text: TEXT;

CDATA: '<![CDATA[' .*? ']]>';

TAG_NAME: TAG_NameStartChar* TAG_NameChar* SYMBOL*;
SYMBOL: ('!' | '@' |'#' |'$' |'%' |'^' |'&' |'*' | '(' | ')');
fragment TAG_NameChar: TAG_NameStartChar | '-' | '_' | '.' | DIGIT |'\u00B7' |'\u0300'..'\u036F' |'\u203F'..'\u2040';
fragment TAG_NameStartChar: [:a-zA-Z] |'\u2070'..'\u218F' |'\u2C00'..'\u2FEF' |'\u3001'..'\uD7FF' |'\uF900'..'\uFDCF' |'\uFDF0'..'\uFFFD';
fragment DIGIT: [0-9];




// COMMENTS
BLOCK_COMMENT: ('<!--' TAG_NAME '-->');
STRING : ('"'~('\r' | '\n' | '"')*'"' | '\''~('\r' | '\n' | '"')*'\'') ;

// Skip whitespace
WS: [ \t\n\r]+ -> channel(HIDDEN);
