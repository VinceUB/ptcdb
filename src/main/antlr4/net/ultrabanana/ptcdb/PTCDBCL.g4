grammar PTCDBCL;

config: (entry NEWLINE*)* EOF ;

entry: title kvPair*;

title: cardName TITLE_UNDERSCORE;

kvPair: KEY value? ;

value : multiLine | sameLine ;

multiLine: MULTILINE_START multiLineContent MULTILINE_END;
sameLine: (WHITESPACE* word WHITESPACE*)+ ;

multiLineContent: (sameLine NEWLINE?)* ;

word: name | OTHER_WORD | cardName ;

name: OWNER ;
cardName: CARD_NAME ;

fragment WS: '\t' | ' ';
fragment NL: ( ('\r'? '\n') | '\r' );
fragment ALPHA: [A-Za-z];
fragment W: ~[\n\r\t ]+;

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

CARD_NAME: [A-Z] W WS 'Martin' ;

//SAMELINE: ~[\n\r]+ ;
OTHER_WORD: W ;

NEWLINE: NL;
WHITESPACE: WS;
