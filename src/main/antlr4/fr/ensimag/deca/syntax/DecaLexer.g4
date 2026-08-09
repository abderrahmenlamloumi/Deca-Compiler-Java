lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
// Keywords
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';
ASSERT: 'assert';

// Identifiers
fragment Letter: [a-zA-Z];
IDENT: (Letter | '$' | '_') (Letter | Digit | '$' | '_')*;

// Literals
INT: '0' | PositiveDigit Digit*;
FLOAT: FloatDec | FloatHex;
fragment Digit: [0-9];
fragment PositiveDigit: [1-9];
fragment Num: Digit+;
fragment Sign: '+' | '-';
fragment Exp: ('E' | 'e') Sign? Num;
fragment Dec: Num '.' Num;
fragment FloatDec: (Dec | Dec Exp) ('F' | 'f')?;
fragment DigitHex: [0-9a-fA-F];
fragment NumHex: DigitHex+;
fragment FloatHex: ('0x' | '0X') NumHex '.' NumHex ('P' | 'p') Sign? Num ('F' | 'f')?;

// String literals
STRING: '"' (StringCharacter | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (StringCharacter | EOL | '\\"' | '\\\\')* '"';
fragment StringCharacter: ~["\\\n];
fragment EOL: '\r'? '\n';

//
fragment FILENAME: (Letter | Digit | '.' | '-' | '_')+;
INCLUDE: '#include' (' ')* '"' FILENAME '"'{
    doInclude(getText());
};

// Separators
OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
OBRACKET: '[';
CBRACKET: ']';
SEMI: ';';
COMMA: ',';
DOT: '.';

// Operators
EQUALS: '=';
GT: '>';
LT: '<';
EXCLAM: '!';
EQEQ: '==';
LEQ: '<=';
GEQ: '>=';
NEQ: '!=';
AND: '&&';
OR: '||';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';

// Comments
WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);
