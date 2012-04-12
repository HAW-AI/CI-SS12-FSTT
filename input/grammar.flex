package haw.ai.ci;

import static haw.ai.ci.TokenID.*;


%%

%public
%class MyScanner
%line
%column
%type Token

	
digit =	[0-9]
letter=	[a-zA-Z]
id =	{letter}({letter}|{digit}|"_"})*
BLANK=[ \t\n\r]

%% 
"*"		{ return new Token(MUL, yytext(), yyline, yycolumn); }
"+"		{ return new Token(PLUS, yytext(), yyline, yycolumn); }
"-"		{ return new Token(MINUS, yytext(), yyline, yycolumn); }
"/"		{ return new Token(DIV, yytext(), yyline, yycolumn); }
":="	{ return new Token(ASSIGN, yytext(), yyline, yycolumn); }

"="		{ return new Token(EQ, yytext(), yyline, yycolumn); }
"#"		{ return new Token(NEQ, yytext(), yyline, yycolumn); }
"<"		{ return new Token(LO, yytext(), yyline, yycolumn); }
"<="	{ return new Token(LOEQ, yytext(), yyline, yycolumn); }
">"		{ return new Token(HI, yytext(), yyline, yycolumn); }
">="	{ return new Token(HIEQ, yytext(), yyline, yycolumn); }

"."		{ return new Token(DOT, yytext(), yyline, yycolumn); }
","		{ return new Token(COMMA, yytext(), yyline, yycolumn); }
":"		{ return new Token(COLON, yytext(), yyline, yycolumn); }
"("		{ return new Token(LPAR, yytext(), yyline, yycolumn); }
")"		{ return new Token(RPAR, yytext(), yyline, yycolumn); }
"["		{ return new Token(LBRAC, yytext(), yyline, yycolumn); }
"]"		{ return new Token(RBRAC, yytext(), yyline, yycolumn); }
";"		{ return new Token(SEMICOLON, yytext(), yyline, yycolumn); }

[oO][fF]				{ return new Token(OF, yytext(), yyline, yycolumn); }
[tT][hH][eE][nN]		{ return new Token(THEN, yytext(), yyline, yycolumn); }
[dD][oO]				{ return new Token(DO, yytext(), yyline, yycolumn); }
[pP][rR][iI][nN][tT]	{ return new Token(PRINT, yytext(), yyline, yycolumn); }
[rR][eE][aA][dD]		{ return new Token(READ, yytext(), yyline, yycolumn); }

[eE][nN][dD]				{ return new Token(END, yytext(), yyline, yycolumn); }
[eE][lL][sS][eE]			{ return new Token(ELSE, yytext(), yyline, yycolumn); }
[eE][lL][sS][iI][fF]		{ return new Token(ELSIF, yytext(), yyline, yycolumn); }
[iI][fF]					{ return new Token(IF, yytext(), yyline, yycolumn); }
[wW][hH][iI][lL][eE]		{ return new Token(WHILE, yytext(), yyline, yycolumn); }
[rR][eE][pP][eE][eA][tT]	{ return new Token(REPEAT, yytext(), yyline, yycolumn); }
[uU][nN][tT][iI][lL]		{ return new Token(UNTIL, yytext(), yyline, yycolumn); }

[aA][rR][rR][aA][yY]		{ return new Token(ARRAY, yytext(), yyline, yycolumn); }
[rR][eE][cC][oO][rR][dD]	{ return new Token(RECORD, yytext(), yyline, yycolumn); }
[cC][oO][nN][sS][tT]		{ return new Token(CONST, yytext(), yyline, yycolumn); }
[tT][yY][pP][eE]			{ return new Token(TYPE, yytext(), yyline, yycolumn); }
	
[vV][aA][rR]							{ return new Token(VAR, yytext(), yyline, yycolumn); }
[pP][rR][oO][cC][eE][dD][uU][rR][eE]	{ return new Token(PROCEDURE, yytext(), yyline, yycolumn); }
[bB][eE][gG][iI][nN]					{ return new Token(BEGIN, yytext(), yyline, yycolumn); }
[mM][oO][dD][uU][lL][eE]				{ return new Token(MODULE, yytext(), yyline, yycolumn); }



-?{digit}+ 	{ return new Token(INT, yytext(), yyline, yycolumn); }
{id}		{ return new Token(IDENT, yytext(), yyline, yycolumn); }

{BLANK} {}
. { System.out.println("error (" + yytext() + "," + yyline + "," +  yycolumn +")"); System.exit(0);}