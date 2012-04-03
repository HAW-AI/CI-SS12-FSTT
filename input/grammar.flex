package haw.ai.ci;

import static haw.ai.ci.TokenID.*;


%%

%public
%class MyScanner
%standalone
%line
%column

	
digit =	[0-9]
letter=	[a-zA-Z]
id =	{letter}({letter}|{digit}|"_"})*
BLANK=[ \t\n\r]

%% 
"*"		{ new Token(MUL, yytext(), yyline, yycolumn); }
"+"		{ new Token(PLUS, yytext(), yyline, yycolumn); }
"-"		{ new Token(MINUS, yytext(), yyline, yycolumn); }
"/"		{ new Token(DIV, yytext(), yyline, yycolumn); }
":="	{ new Token(ASSIGN, yytext(), yyline, yycolumn); }

"="		{ new Token(EQ, yytext(), yyline, yycolumn); }
"#"		{ new Token(NEQ, yytext(), yyline, yycolumn); }
"<"		{ new Token(LO, yytext(), yyline, yycolumn); }
"<="	{ new Token(LOEQ, yytext(), yyline, yycolumn); }
">"		{ new Token(HI, yytext(), yyline, yycolumn); }
">="	{ new Token(HIEQ, yytext(), yyline, yycolumn); }

"."		{ new Token(DOT, yytext(), yyline, yycolumn); }
","		{ new Token(COMMA, yytext(), yyline, yycolumn); }
":"		{ new Token(COLON, yytext(), yyline, yycolumn); }
"("		{ new Token(LPAR, yytext(), yyline, yycolumn); }
")"		{ new Token(RPAR, yytext(), yyline, yycolumn); }
"["		{ new Token(LBRAC, yytext(), yyline, yycolumn); }
"]"		{ new Token(RBRAC, yytext(), yyline, yycolumn); }
";"		{ new Token(SEMICOLON, yytext(), yyline, yycolumn); }

[oO][fF]				{ new Token(OF, yytext(), yyline, yycolumn); }
[tT][hH][eE][nN]		{ new Token(THEN, yytext(), yyline, yycolumn); }
[dD][oO]				{ new Token(DO, yytext(), yyline, yycolumn); }
[pP][rR][iI][nN][tT]	{ new Token(PRINT, yytext(), yyline, yycolumn); }
[rR][eE][aA][dD]		{ new Token(READ, yytext(), yyline, yycolumn); }

[eE][nN][dD]				{ new Token(END, yytext(), yyline, yycolumn); }
[eE][lL][sS][eE]			{ new Token(ELSE, yytext(), yyline, yycolumn); }
[eE][lL][sS][iI][fF]		{ new Token(ELSIF, yytext(), yyline, yycolumn); }
[iI][fF]					{ new Token(IF, yytext(), yyline, yycolumn); }
[wW][hH][iI][lL][eE]		{ new Token(WHILE, yytext(), yyline, yycolumn); }
[rR][eE][pP][eE][eA][tT]	{ new Token(REPEAT, yytext(), yyline, yycolumn); }
[uU][nN][tT][iI][lL]		{ new Token(UNTIL, yytext(), yyline, yycolumn); }

[aA][rR][rR][aA][yY]		{ new Token(ARRAY, yytext(), yyline, yycolumn); }
[rR][eE][cC][oO][rR][dD]	{ new Token(RECORD, yytext(), yyline, yycolumn); }
[cC][oO][nN][sS][tT]		{ new Token(CONST, yytext(), yyline, yycolumn); }
[tT][yY][pP][eE]			{ new Token(TYPE, yytext(), yyline, yycolumn); }
	
[vV][aA][rR]							{ new Token(VAR, yytext(), yyline, yycolumn); }
[pP][rR][oO][cC][eE][dD][uU][rR][eE]	{ new Token(PROCEDURE, yytext(), yyline, yycolumn); }
[bB][eE][gG][iI][nN]					{ new Token(BEGIN, yytext(), yyline, yycolumn); }
[mM][oO][dD][uU][lL][eE]				{ new Token(MODULE, yytext(), yyline, yycolumn); }



{digit}+ 	{ System.out.println("DIGITS " + yytext()); }
{id}		{ System.out.println("ID " + yytext()); }

{BLANK} {}
. { System.out.println("error (" + yytext() + "," + yyline + "," +  yycolumn +")"); System.exit(0);}