grammar FlourineScript;

// Parser
compilationUnit
    :   statement* EOF
    ;

statement
    :   variableDeclStatement
    |   solveStatement
    ;

variableDeclStatement
    :   KEYWORD_LET IDENTIFIER ASSIGN expression
    ;

solveStatement
    :   KEYWORD_SOLVE IDENTIFIER
    |   KEYWORD_SOLVE expression
    ;

expression
    :   conditionalExpression
    ;

conditionalExpression
    :   conditionalLogicExpression (QUESTION expression COLON expression)?
    ;

conditionalLogicExpression
    :   relationalExpression conditionalLogicExpressionRest*
    ;

conditionalLogicExpressionRest
    :   (AND | OR) relationalExpression
    ;

relationalExpression
    :   additiveExpression ((LT | GT | LE | GE | EQUAL | NOTEQUAL) additiveExpression)?
    ;

additiveExpression
    :   multiplicativeExpression additiveExpressionRest*
    ;

additiveExpressionRest
    :   (ADD | SUB) multiplicativeExpression
    ;

multiplicativeExpression
    :   unaryExpression multiplicativeExpressionRest*
    ;

multiplicativeExpressionRest
    :   (MUL | DIV | MOD | POWER) unaryExpression
    ;

unaryExpression
    :   (ADD | SUB) unaryExpression
    |   BANG unaryExpression
    |   primaryExpression;

primaryExpression : primaryPrefix primarySuffix*;

primaryPrefix
    :   literalExpression
    |   quotedExpression
    |   IDENTIFIER
    |   pointExpression
    |   listExpression
    |   lambdaExpression
    ;

primarySuffix
    :   lambdaCallExpression
    |   listVisitExpression
    ;

lambdaCallExpression
    :   LPAREN argumentList RPAREN
    ;

listVisitExpression
    :   LBRACK additiveExpression RBRACK
    ;

literalExpression
    :   NumberLiteral
    |   StringLiteral
    |   CharacterLiteral
    |   BooleanLiteral
    ;

quotedExpression
    :   LPAREN conditionalLogicExpression RPAREN
    ;

pointExpression
    :   LPAREN expression COMMA expression (COMMA expression)? RPAREN
    ;

listExpression
    :   LBRACE (expression (COMMA expression)*)? COMMA? RBRACE
    ;

lambdaExpression
    :   LBRACK RBRACK LPAREN parameterList RPAREN ARROW lambdaBody
    ;

lambdaBody
    :   LBRACE statement* expression RBRACE
    ;

argumentList
    :   (expression (COMMA expression)* (COMMA ELLIPSIS)?)?
    |   (ELLIPSIS)?
    ;

parameterList
    :   (IDENTIFIER (COMMA IDENTIFIER)* (COMMA ELLIPSIS)?)?
    |   (ELLIPSIS)?
    ;

// Lexer
KEYWORD_SOLVE: 'solve';
KEYWORD_LET: 'let';
KEYWORD_LAMBDA: 'lambda';

// Literal
NumberLiteral
    :   IntegerLiteral
    |   FloatingPointLiteral
    ;

IntegerLiteral
	:	DecimalIntegerLiteral
	|	HexIntegerLiteral
	|	OctalIntegerLiteral
	|	BinaryIntegerLiteral
	;

fragment
DecimalIntegerLiteral
	:	DecimalNumeral IntegerTypeSuffix?
	;

fragment
HexIntegerLiteral
	:	HexNumeral IntegerTypeSuffix?
	;

fragment
OctalIntegerLiteral
	:	OctalNumeral IntegerTypeSuffix?
	;

fragment
BinaryIntegerLiteral
	:	BinaryNumeral IntegerTypeSuffix?
	;

fragment
IntegerTypeSuffix
	:	[lL]
	;

fragment
DecimalNumeral
	:	'0'
	|	NonZeroDigit (Digits? | Underscores Digits)
	;

fragment
Digits
	:	Digit (DigitsAndUnderscores? Digit)?
	;

fragment
Digit
	:	'0'
	|	NonZeroDigit
	;

fragment
NonZeroDigit
	:	[1-9]
	;

fragment
DigitsAndUnderscores
	:	DigitOrUnderscore+
	;

fragment
DigitOrUnderscore
	:	Digit
	|	'_'
	;

fragment
Underscores
	:	'_'+
	;

fragment
HexNumeral
	:	'0' [xX] HexDigits
	;

fragment
HexDigits
	:	HexDigit (HexDigitsAndUnderscores? HexDigit)?
	;

fragment
HexDigit
	:	[0-9a-fA-F]
	;

fragment
HexDigitsAndUnderscores
	:	HexDigitOrUnderscore+
	;

fragment
HexDigitOrUnderscore
	:	HexDigit
	|	'_'
	;

fragment
OctalNumeral
	:	'0' Underscores? OctalDigits
	;

fragment
OctalDigits
	:	OctalDigit (OctalDigitsAndUnderscores? OctalDigit)?
	;

fragment
OctalDigit
	:	[0-7]
	;

fragment
OctalDigitsAndUnderscores
	:	OctalDigitOrUnderscore+
	;

fragment
OctalDigitOrUnderscore
	:	OctalDigit
	|	'_'
	;

fragment
BinaryNumeral
	:	'0' [bB] BinaryDigits
	;

fragment
BinaryDigits
	:	BinaryDigit (BinaryDigitsAndUnderscores? BinaryDigit)?
	;

fragment
BinaryDigit
	:	[01]
	;

fragment
BinaryDigitsAndUnderscores
	:	BinaryDigitOrUnderscore+
	;

fragment
BinaryDigitOrUnderscore
	:	BinaryDigit
	|	'_'
	;

// Floating-Point Literals

FloatingPointLiteral
	:	DecimalFloatingPointLiteral
	|	HexadecimalFloatingPointLiteral
	;

fragment
DecimalFloatingPointLiteral
	:	Digits '.' Digits? ExponentPart? FloatTypeSuffix?
	|	'.' Digits ExponentPart? FloatTypeSuffix?
	|	Digits ExponentPart FloatTypeSuffix?
	|	Digits FloatTypeSuffix
	;

fragment
ExponentPart
	:	ExponentIndicator SignedInteger
	;

fragment
ExponentIndicator
	:	[eE]
	;

fragment
SignedInteger
	:	Sign? Digits
	;

fragment
Sign
	:	[+-]
	;

fragment
FloatTypeSuffix
	:	[fFdD]
	;

fragment
HexadecimalFloatingPointLiteral
	:	HexSignificand BinaryExponent FloatTypeSuffix?
	;

fragment
HexSignificand
	:	HexNumeral '.'?
	|	'0' [xX] HexDigits? '.' HexDigits
	;

fragment
BinaryExponent
	:	BinaryExponentIndicator SignedInteger
	;

fragment
BinaryExponentIndicator
	:	[pP]
	;

// Boolean Literals

BooleanLiteral
	:	'true'
	|	'false'
	;

// Character Literals

CharacterLiteral
	:	'\'' SingleCharacter '\''
	|	'\'' EscapeSequence '\''
	;

fragment
SingleCharacter
	:	~['\\\r\n]
	;

// String Literals
StringLiteral
	:	'"' StringCharacters? '"'
	;
fragment
StringCharacters
	:	StringCharacter+
	;
fragment
StringCharacter
	:	~["\\\r\n]
	|	EscapeSequence
	;
// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
	:	'\\' [btnfr"'\\]
	|	OctalEscape
    |   UnicodeEscape // This is not in the spec but prevents having to preprocess the input
	;

fragment
OctalEscape
	:	'\\' OctalDigit
	|	'\\' OctalDigit OctalDigit
	|	'\\' ZeroToThree OctalDigit OctalDigit
	;

fragment
ZeroToThree
	:	[0-3]
	;

// This is not in the spec but prevents having to preprocess the input
fragment
UnicodeEscape
    :   '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

// Separators

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';
ELLIPSIS : '...';
AT : '@';
COLONCOLON : '::';


// Operators

ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
QUESTION : '?';
COLON : ':';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
POWER : '^';
MOD : '%';
ARROW : '->';

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';

// §3.8 Identifiers (must appear after all keywords in the grammar)

IDENTIFIER
	:	ScriptLetter ScriptLetterOrDigit*
	;

fragment
ScriptLetter
	:	[a-zA-Z$_] // these are the "java letters" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment
ScriptLetterOrDigit
	:	[a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
	|	// covers all characters above 0x7F which are not a surrogate
		~[\u0000-\u007F\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(HIDDEN)
    ;
