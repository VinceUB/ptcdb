grammar PTCDBCL;

config: (entry NEWLINE*)* EOF ;

entry: title kvPair*;

title: word WHITESPACE MARTIN TITLE_UNDERSCORE;

kvPair: KEY value? ;

value : multiLine | sameLine ;

multiLine: MULTILINE_START (sameLine NEWLINE?)* MULTILINE_END;
sameLine: (WHITESPACE* word WHITESPACE*)+ ;

word: name | OTHER_WORD ;

name: OWNER | MARTIN ;

fragment WS: '\t' | ' ';
fragment NL: ( ('\r'? '\n') | '\r' );
fragment ALPHA: [A-Za-z];

KEY: NL ALPHA+ WS* ':' WS* ;

MULTILINE_START: WS* '"""' WS* NL ;
MULTILINE_END: NL WS* '"""' WS* ;

TITLE: NL ~[\t ]* WS 'Martin' WS*;
TITLE_UNDERSCORE: NL '-'+ WS* ;

OWNER:
	'Sebastian' | 
	'Patrick'   |
	'Vincent'   |
	'Kresten'   | 
	'Sille'     |
	'Amalie'    |
	'Topholt'   ;

MARTIN: 'Martin' ;

//SAMELINE: ~[\n\r]+ ;
OTHER_WORD: ~[\n\r\t ]+ ;

NEWLINE: NL;
WHITESPACE: WS;
