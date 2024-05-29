grammar PTCDBCL;

config: (NEWLINE* entry NEWLINE*)* EOF ;

entry: title kvPair*;

title: titleCardName TITLE_UNDERSCORE;

titleCardName: CARD_NAME;

kvPair: KEY value? ;

value : multiLine | sameLine ;

multiLine: MULTILINE_START multiLineContent MULTILINE_END;
sameLine: (strWhitespace* word strWhitespace*)+ ;

multiLineContent: (sameLine strNewline?)* ;

strWhitespace: WHITESPACE;
strNewline: NEWLINE;

word:  name | normalWord | cardName ;

name: OWNER ;
cardName: CARD_NAME ;
normalWord: OTHER | OTHER* OTHER_WORD OTHER*;

fragment WS: '\t' | ' ';
fragment NL: ( ('\r'? '\n') | '\r' );
fragment ALPHA: [A-Za-zÆØÅæøåÄÖÜäöü];
//fragment W: ~[\n\r\t ]+;

COMMENT: '<!--' .*? '-->' -> skip;

KEY: NL ALPHA+ WS* ':' WS* ;

MULTILINE_START: WS* '"""' WS* NL ;
MULTILINE_END: NL WS* '"""' WS* ;

TITLE_UNDERSCORE: NL '-'+ WS* ;

OWNER:
	'Sebastian' | 
	'Patrick'   |
	'Vincent'   |
	'Kresten'   | 
	'Sille'     |
	'Amalie'    |
	'Topholt'   ;

CARD_NAME: [A-Z] ALPHA+ WS 'Martin' ;

//SAMELINE: ~[\n\r]+ ;
OTHER_WORD: ALPHA+ ;

NEWLINE: NL;
WHITESPACE: WS;

OTHER: .;