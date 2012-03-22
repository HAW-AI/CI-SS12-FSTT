package haw.ai.ci;


 class Token {
	private int id, line, column;
	private String text;
	
	public Token(int id, String text, int line, int column) {
		this.id = id;
		this.text = text;
		this.line = line;
		this.column = column;
		
		String out = "Token(" + text + "," + line + "," + column + ")";
		System.out.println(out);
	}
	
	public int id() { return id; }
	public String text() { return text; }
	public int line() { return line; }
	public int column() { return column; }
}



	

%%

%public
%class MyScanner
%standalone
%line
%column


%{
  public static final int 	MUL = 256;
  public static final int 	PLUS = 257;
  public static final int 	MINUS = 258;
  public static final int 	DIV = 259;
  public static final int 	ASSIGN = 260;
  public static final int 	EQ = 261;
  public static final int 	NEQ = 262;
  
public static final int LO = 263;
public static final int DO = 295;
public static final int LOEQ = 264;
public static final int HI = 265;
public static final int HIEQ = 267;
public static final int DOT = 268;
public static final int COMMA = 269;
public static final int COLON = 270;
public static final int LPAR = 271;
public static final int RPAR = 272;
public static final int LBRAC = 273;
public static final int RBRAC = 274;
public static final int SEMICOLON = 275;
public static final int OF = 276;
public static final int THEN = 277;
public static final int PRINT = 278;
public static final int READ = 279;
public static final int END = 280;
public static final int ELSE = 281;
public static final int ELSIF = 282;
public static final int IF = 283;
public static final int WHILE = 284;
public static final int REPEAT = 285;
public static final int UNTIL = 286;
public static final int ARRAY = 287;
public static final int RECORD = 288;
public static final int CONST = 289;
public static final int TYPE = 290;
public static final int VAR = 291;
public static final int PROCEDURE = 292;
public static final int BEGIN = 293;
public static final int MODULE = 294;
%}
	
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