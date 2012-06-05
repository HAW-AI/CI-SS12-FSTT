package haw.ai.ci;

import static haw.ai.ci.node.BinOpNode.BinOp.*;
import static haw.ai.ci.TokenID.*;

import haw.ai.ci.node.*;

import java.util.ArrayList;
import java.util.List;


public class Parser {
	private Token nextSymbol;
	private Scanner scanner;
	private String fileName;
	
	public Parser(Scanner scanner, String fileName) {
		this.scanner = scanner;
		this.fileName = fileName;
		insymbol();
	}

	static void error(String str) {
		throw new ParserException("==> Error: " + str);
	}

	/**
	 * Check if the next token is expectedToken or fail with an error message
	 * otherwise.
	 * 
	 * An expectedToken value of null will verify that the remaining scanner
	 * output is empty.
	 * 
	 * @param expectedToken
	 *            the expected token
	 * @param expectedString
	 *            the expected token in human readable form
	 */
	void expect(TokenID expectedToken, String expectedString) {
		if (!test(expectedToken)) {
			failExpectation(expectedString);
		}
	}

	/**
	 * Stop parsing and show the provided error message together with the
	 * current line and column.
	 * 
	 * @param expectedString
	 *            the expected token(s) in human readable form
	 */
	void failExpectation(String expectation) {
		String location;

		if (nextSymbol != null) {
			location = "line " + (nextSymbol.line() + 1) + ", column " + (nextSymbol.column() + 1);
		} else {
			location = "end of file";
		}

		error("expected " + expectation + " at " + location);
	}

	/**
	 * Check if the next token is of the same type as the provided token.
	 * 
	 * A token value of null will test if the remaining scanner output is empty.
	 * 
	 * @param token
	 *            the token to test against
	 * @return true, if the next token is of the same type as the provided
	 *         token, false otherwise
	 */
	boolean test(TokenID token) {
		return nextSymbol == null ? token == null : nextSymbol.id() == token;
	}

	/**
	 * Read the next token from the scanner and verify that its of type
	 * expectedToken. If it's not then fail with expectedString.
	 * 
	 * A token value of null will test if the remaining scanner output is empty.
	 * 
	 * @param expectedToken
	 *            the expected token
	 * @param expectedString
	 *            the expected token in human readable form
	 * @return the next token
	 */
	Token read(TokenID expectedToken, String expectedString) {
		expect(expectedToken, expectedString);
		return read();
	}

	/**
	 * Read the next token from the scanner.
	 * 
	 * @return the next token or null if the scanner output is empty
	 */
	Token read() {
		Token curSymbol = nextSymbol;
		insymbol();
		return curSymbol;
	}

	/**
	 * Check if the token after the next token is of the same type as the
	 * provided token.
	 * 
	 * A token value of null will test if the remaining scanner output is empty.
	 * 
	 * @param token
	 *            the token to test against
	 * @return true, if the token after the next token is of the same type as
	 *         the provided token, false otherwise
	 */
	boolean testLookAhead(TokenID token) {
		Token afterNextSymbol = null;

		try {
			afterNextSymbol = scanner.yylex();
			if (afterNextSymbol != null) {
				scanner.yypushback(afterNextSymbol.text().length());
			}
		} catch (java.lang.Error e) {
			// will be thrown when next symbol is EOF
			// in this case null is a good value for afterNextSymbol
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File not found : \"" + fileName + "\"");
		} catch (java.io.IOException e) {
			System.out.println("IO error scanning file \"" + fileName + "\"");
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("Unexpected exception:");
			e.printStackTrace();
		}

		return afterNextSymbol == null ? token == null : afterNextSymbol.id() == token;
	}

	public void insymbol() {
		try {
			nextSymbol = scanner.yylex();
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File not found : \"" + fileName + "\"");
		} catch (java.io.IOException e) {
			System.out.println("IO error scanning file \"" + fileName + "\"");
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("Unexpected exception:");
			e.printStackTrace();
		}
	}

	IdentNode constIdent() {
		return new IdentNode(read(IDENT, "identifier").text());
	}

	IntNode integer() {
		return new IntNode(Integer.valueOf(read(INT, "integer").text()));
	}

	StringNode string() {
		String str = read(STR, "string").text();
		return new StringNode(str.substring(1, str.length() - 1));
	}
	
	ReadNode readParser() {
	    read(READ, "READ");
	    return test(STR) ? new ReadNode(string()) : new ReadNode();
	}

	AbstractNode indexExpr() {
		AbstractNode node = null;

		if (test(INT)) {
			node = integer();
		} else if (test(IDENT)) {
			node = new ContentNode(constIdent());
		} else {
			failExpectation("integer or identifier");
		}

		return node;
	}

	SelectorNode selector() {
		IdentNode subject = constIdent();
		SelectorNode node = null;
		
		if (test(DOT)) {
		    read(DOT, ".");
		    node = new RecordSelectorNode(subject, constIdent());
		} else if (test(LBRAC)) {
		    read(LBRAC, "[");
		    node = new ArraySelectorNode(subject, expr());
            read(RBRAC, "]");
		} else {
		    failExpectation(". or [");
		}

		while (test(DOT) || test(LBRAC)) {
			if (test(DOT)) {
				read(DOT, ".");
				node = new RecordSelectorNode(node, constIdent());
			} else {
				read(LBRAC, "[");
				node = new ArraySelectorNode(node, expr());
				read(RBRAC, "]");
			}
		}

		return node;
	}

	// Assignment = ident Selector �:=� Expression.
	AssignmentNode assignment() {
		AssignmentNode node = null;
		AbstractNode expr = null;

		if (testLookAhead(DOT) || testLookAhead(LBRAC)) {
			SelectorNode selector = selector();
			read(ASSIGN, ":=");
			expr = expr();
			node = new AssignmentNode(selector, expr);
		} else {
			IdentNode ident = constIdent();
			read(ASSIGN, ":=");
			expr = expr();
			node = new AssignmentNode(ident, expr);
		}
		
		return node;
	}

	// ActualParameters = Expression {�,� Expression}
	ActualParametersNode actualParameters() {
		ArrayList<AbstractNode> list = new ArrayList<AbstractNode>();

		list.add(expr());
		while (test(COMMA)) {
			read(COMMA, ",");
			list.add(expr());
		}
		
		return new ActualParametersNode(list);
	}

	// ProcedureCall = ident �(� [ActualParameters] �)�.

	ProcedureCallNode procedureCall() {
		IdentNode ident = null;
		AbstractNode actualParameters = null;

		ident = constIdent();
		read(LPAR, "(");
		if(!test(RPAR))
			actualParameters = actualParameters();
		read(RPAR, ")");
		return new ProcedureCallNode(ident, actualParameters);
	}

	// IfStatement = �IF� Expression
	// �THEN� StatementSequence
	// {�ELSIF� Expression �THEN�
	// StatementSequence}
	// [�ELSE� StatementSequence] �END�.

	IfStatementNode ifStatement() {
		IfStatementNode node = null;
		AbstractNode exp1 = null;
		AbstractNode stateSeq1 = null;
		AbstractNode stateSeq2 = null;
		// abwechselnd expr und statementSeq
		AbstractNode elseifs = null;

			read(IF, "IF");
			exp1 = expr();
			read(THEN, "THEN");
			stateSeq1 = statementSequence();
			if (test(ELSIF)) {
				elseifs = ifStatement_();
			}
			if (test(END)) {
				read(END, "END");
				node = new IfStatementNode(exp1, stateSeq1, elseifs, stateSeq2);
			} else {
				read(ELSE, "ELSE");
				stateSeq2 = statementSequence();
				read(END, "END");
				node = new IfStatementNode(exp1, stateSeq1, elseifs, stateSeq2);
			}
		

		return node;

	}
	IfStatementNode ifStatement_() {
		IfStatementNode node = null;
		AbstractNode exp1 = null;
		AbstractNode stateSeq1 = null;

		read(ELSIF, "ELSIF");
		exp1 = expr();
		read(THEN, "THEN");
		stateSeq1 = statementSequence();
		if(test(ELSIF)){
		node = new IfStatementNode(exp1, stateSeq1,ifStatement_(), null);
		}else{
		node = new IfStatementNode(exp1, stateSeq1, null, null);
		}
		
		return node;
	}

	// WhileStatement = �WHILE� Expression �DO� StatementSequence �END�.

	WhileStatementNode whileStatement() {
		AbstractNode exp1 = null;
		AbstractNode stateSeq1 = null;

		read(WHILE, "WHILE");
		exp1 = expr();
		read(DO, "DO");
		stateSeq1 = statementSequence();
		read(END, "END");
		return new WhileStatementNode(exp1, stateSeq1);
	}

	// RepeatStatement = �REPEAT� StatementSequence �UNTIL� Expression.

	RepeatStatementNode repeatStatement() {
		AbstractNode exp1 = null;
		AbstractNode stateSeq1 = null;

		read(REPEAT, "REPEAT");
		stateSeq1 = statementSequence();
		read(UNTIL, "UNTIL");
		exp1 = expr();
		return new RepeatStatementNode(stateSeq1, exp1);
	}

	// Statement = [Assignment | ProcedureCall | IfStatement | �PRINT�
	// Expression | WhileStatement | RepeatStatement].

	AbstractNode statement() {
		AbstractNode node = null;

		if (test(IF)) {
			node = ifStatement();
		} else if (test(PRINT)) {
			read(PRINT, "PRINT");
			node = new PrintNode(expr());
		} else if (test(WHILE)) {
			node = whileStatement();
		} else if (test(REPEAT)) {
			node = repeatStatement();
		} else if (test(IDENT)) {
            if (testLookAhead(LPAR)) {
                node = procedureCall();
            } else {
				node = assignment();
			}
		}

		return node;
	}

	// StatementSequence = Statement {�;� Statement}.

	StatementSequenceNode statementSequence() {
		ArrayList<AbstractNode> list = new ArrayList<AbstractNode>();
		AbstractNode stmt = statement();
		if (stmt != null) {
		    list.add(stmt);
		}
//		while (!testLookAhead(END) && !testLookAhead(ELSE) && !testLookAhead(ELSIF) && !testLookAhead(UNTIL)) {
		while (test(SEMICOLON)) {
			read(SEMICOLON, ";");
			stmt = statement();
			if (stmt != null) {
			    list.add(stmt);
			}
		}
		return new StatementSequenceNode(list);
	}

	AbstractNode factor() {
		AbstractNode node = null;

		if (test(IDENT)) {
			if (testLookAhead(DOT) || testLookAhead(LBRAC)) {
				node = selector();
			} else {
				node = new ContentNode(constIdent());
			}
		} else if (test(INT)) {
			node = integer();
		} else if (test(STR)) {
			node = string();
		} else if (test(READ)) {
			node = readParser();
		} else if (test(LPAR)) {
			read(LPAR, "(");
			node = expr();
			read(RPAR, ")");
		} else {
			failExpectation("identifier, integer, string, read or (expression)");
		}

		return node;
	}

	AbstractNode term() {
		AbstractNode node = factor();

		while (test(MUL) || test(DIV)) {
			if (test(MUL)) {
				read(MUL, "*");
				node = new BinOpNode(MUL_OP, node, factor());
			} else if (test(DIV)) {
				read(DIV, "/");
				node = new BinOpNode(DIV_OP, node, factor());
			}
		}

		return node;
	}

	AbstractNode simpleExpr() {
		AbstractNode node;

		if (test(MINUS)) {
			read(MINUS, "-");
			node = new NegationNode(term());
		} else {
			node = term();
		}

		while (test(PLUS) || test(MINUS)) {
			if (test(PLUS)) {
				read(PLUS, "+");
				node = new BinOpNode(PLUS_OP, node, term());
			} else if (test(MINUS)) {
				read(MINUS, "-");
				node = new BinOpNode(MINUS_OP, node, term());
			}
			
		}

		return node;
	}

	AbstractNode expr() {
		AbstractNode node = simpleExpr();

		if (test(EQ)) {
			read(EQ, "=");
			node = new BinOpNode(EQ_OP, node, simpleExpr());
		} else if (test(NEQ)) {
			read(NEQ, "#");
			node = new BinOpNode(NEQ_OP, node, simpleExpr());
		} else if (test(LO)) {
			read(LO, "<");
			node = new BinOpNode(LO_OP, node, simpleExpr());
		} else if (test(LOEQ)) {
			read(LOEQ, "<=");
			node = new BinOpNode(LOEQ_OP, node, simpleExpr());
		} else if (test(HI)) {
			read(HI, ">");
			node = new BinOpNode(HI_OP, node, simpleExpr());
		} else if (test(HIEQ)) {
			read(HIEQ, ">=");
			node = new BinOpNode(HIEQ_OP, node, simpleExpr());
		}

		return node;
	}

//	ProcedureDeclarationNode procedureDeclaration() {
//		// ProcedureDeclaration = ProcedureHeading �;� ProcedureBody ident
//		
//		AbstractNode procHeadingNode = procedureHeading();
//		read(SEMICOLON, ";");
//		AbstractNode procBodyNode = procedureBody();
//		IdentNode identNode = constIdent(); // ident kann hier unterschiedlich vom ident im procHeading sein. ist nur syntaxpr�fung
//
//		ProcedureDeclarationNode node = new ProcedureDeclarationNode(procHeadingNode, procBodyNode, identNode);
//
//		return node;
//	}

	DeclarationsNode declaration() {
		// Declarations = [�CONST� ident �=� Expression �;� {ident �=�
		// Expression �;�}]
		// [�TYPE� ident �=� Type �;� {ident �=� Type �;�}]
		// [�VAR� IdentList �:� Type �;� {IdentList �:� Type �;�}]
		// {ProcedureDeclaration �;�}
		
		ArrayList<ConstDeclarationNode> consts = new ArrayList<ConstDeclarationNode>();
	    List<TypeDeclarationNode> types = new ArrayList<TypeDeclarationNode>();
	    List<VarDeclarationNode> vars = new ArrayList<VarDeclarationNode>();
	    List<AbstractNode> procDeclarations = new ArrayList<AbstractNode>();
	    AbstractNode arg1;
	    AbstractNode arg2;

		if (test(CONST)) {
			read(CONST, "const");
			arg1 = constIdent();
			read(EQ, "=");
			arg2 = expr();
			read(SEMICOLON, ";");
			consts.add(new ConstDeclarationNode(arg1,arg2));
			while (test(IDENT)) {
				arg1 = constIdent();
				read(ASSIGN, "=");
				arg2 = expr();
				read(SEMICOLON, ";");
				consts.add(new ConstDeclarationNode(arg1,arg2));
			}
			
		}  if (test(TYPE)) {
			read(TYPE, "type");
			arg1 = constIdent();
			read(EQ, "=");
			arg2 = type();
			read(SEMICOLON, ";");
			types.add(new TypeDeclarationNode(arg1,arg2));
			while (test(IDENT)) {
				arg1 = constIdent();
				read(ASSIGN, "=");
				arg2 = type();
				read(SEMICOLON, ";");
				types.add(new TypeDeclarationNode(arg1,arg2));
			}
		}  if (test(VAR)) {
			read(VAR, "var");
			arg1 = identList();
			read(COLON, ":");
			arg2 = type();
			read(SEMICOLON, ";");
			vars.add(new VarDeclarationNode(arg1,arg2));
			while (test(IDENT)) {
				arg1 = identList();
				read(COLON, ":");
				arg2 = type();
				read(SEMICOLON, ";");
				vars.add(new VarDeclarationNode(arg1,arg2));
			}
		}
		
		while (test(PROCEDURE)) {
//			procDeclarations.add(procedureDeclaration());
			procDeclarations.add(procedure());
			read(SEMICOLON, ";");
		}
//			failExpectation("const, type, var or procedure declaration");
		return new DeclarationsNode(consts,types,vars,procDeclarations);
	}

	ModuleNode module() {
		// Module = �MODULE� ident �;� Declarations
		// �BEGIN� StatementSequence
		// �END� ident �.�

		read(MODULE, "module");
		IdentNode moduleName = constIdent();
		read(SEMICOLON, "semicolon");
		AbstractNode declaration = declaration();
		read(BEGIN, "begin");
		
		AbstractNode statementSequence = statementSequence();
		read(END, "end");
		IdentNode moduleEndName = constIdent();
		// folgendes eigtl schon vorgegriffen, ist nicht mehr nur syntax pr�fung. aber macht den node einfacher und schadet nicht
		if (!moduleName.equals(moduleEndName)) {
			failExpectation("identifiers of module and end are supposed to be the same");
		}
		read(DOT, ".");

		ModuleNode node = new ModuleNode(moduleName, declaration, statementSequence);

		return node;
	}

	IdentListNode identList() {
		List<IdentNode> idents = new ArrayList<IdentNode>();
		if (test(IDENT)) {
			idents.add(constIdent());
			while (test(COMMA)) {
				read(COMMA, ",");
				idents.add(constIdent());
			}
		} else {
			failExpectation("ident");
		}
		return new IdentListNode(idents);
	}

	ArrayTypeNode arrayType() {
		read(ARRAY, "ARRAY");
		read(LBRAC, "[");
		AbstractNode node = indexExpr();
		read(RBRAC, "]");
		read(OF, "OF");
		AbstractNode t = type();
		return new ArrayTypeNode(node, t);
	}

	/**
	 * If nextSymbol is ident then return FieldListNode else null
	 * 
	 * @return
	 */
	FieldListNode fieldList() {
		if (test(IDENT)) {
			AbstractNode il = identList();
			read(COLON, ":");
			AbstractNode t = type();
			return new FieldListNode(il, t);
		} else
			return null;
	}

	RecordTypeNode recordType() {
		read(RECORD, "RECORD");
		List<FieldListNode> fieldLists = new ArrayList<FieldListNode>();
		FieldListNode node = fieldList();
		if(node!=null)
			fieldLists.add(node);
		while (test(SEMICOLON)) {
			read(SEMICOLON,";");
			node = fieldList();
			if(node!=null)
				fieldLists.add(node);
		}
		read(END, "END");
		return new RecordTypeNode(fieldLists);
	}

	AbstractNode type() {
		AbstractNode node = null;
		if (test(IDENT)) {
			node = constIdent();
		} else if (test(ARRAY)) {
			node = arrayType();
		} else if (test(RECORD)) {
			node = recordType();
		} else {
			failExpectation("type");
		}
		return node;
	}

	FPSectionNode fpSection() {
		if (test(VAR))
			read(VAR, "VAR");
		AbstractNode node = identList();
		read(COLON, ":");
		AbstractNode type = type();
		return new FPSectionNode(node, type);
	}

	FormalParametersNode formalParameters() {
		List<FPSectionNode> fpsections = new ArrayList<FPSectionNode>();
		FPSectionNode fpsection = fpSection();
		fpsections.add(fpsection);
		while (test(SEMICOLON)) {
			read(SEMICOLON, ";");
			fpsection = fpSection();
			fpsections.add(fpsection);
		}
		return new FormalParametersNode(fpsections);
	}

//	ProcedureHeadingNode procedureHeading() {
//		read(PROCEDURE, "PROCEDURE");
//		AbstractNode node = constIdent();
//		FormalParametersNode fparams = null;
//		read(LPAR, "(");
//		if (test(VAR) || test(IDENT)) {
//			fparams = formalParameters();
//		}
//		read(RPAR, ")");
//		return new ProcedureHeadingNode(((IdentNode) node), fparams);
//	}

//	ProcedureBodyNode procedureBody() {
//		AbstractNode declarations = declaration();
//		read(BEGIN,"BEGIN");
//		AbstractNode stateSeq = statementSequence();
//		read(END,"END");
//		return new ProcedureBodyNode(declarations,stateSeq);
//		
//		/*
//		 * AbstractNode node = declarations(); read(BEGIN,"BEGIN"); AbstractNode
//		 * statseq=statementSequence(); return new ProcedureBodyNode(node,
//		 * statseq);
//		 */
//	}
	
	ProcedureNode procedure() {
		// decl
		// ProcedureDeclaration = ProcedureHeading �;� ProcedureBody ident
//		AbstractNode procHeadingNode = procedureHeading();
//		read(SEMICOLON, ";");
//		AbstractNode procBodyNode = procedureBody();
//		IdentNode identNode = constIdent(); // ident kann hier unterschiedlich vom ident im procHeading sein. ist nur syntaxpr�fung
//		ProcedureDeclarationNode node = new ProcedureDeclarationNode(procHeadingNode, procBodyNode, identNode);
//		return node;
		
		// head
		read(PROCEDURE, "PROCEDURE");
		AbstractNode procNameIdentNode = constIdent();
		FormalParametersNode fparams = new FormalParametersNode(new ArrayList<FPSectionNode>());
		read(LPAR, "(");
		if (test(VAR) || test(IDENT)) {
			fparams = formalParameters();
		}
		read(RPAR, ")");
//		return new ProcedureHeadingNode(((IdentNode) node1), fparams);
		
		read(SEMICOLON, ";");
		
		// body
		AbstractNode declarations = declaration();
		read(BEGIN,"BEGIN");
		AbstractNode stateSeq = statementSequence();
		read(END,"END");
//		return new ProcedureBodyNode(declarations,stateSeq);
		
		IdentNode declEndIdentNode = constIdent();
		
		return new ProcedureNode(declEndIdentNode, (IdentNode) procNameIdentNode, fparams, declarations, stateSeq);
	}

	AbstractNode program() {
		AbstractNode tree = module();

		if (nextSymbol != null) {
			failExpectation("EOF or comment");
		}

		return tree;
	}

	public AbstractNode parse() {
		return program();
	}

	public static void main(String argv[]) {
		if (argv.length == 0) {
			System.out.println("Usage : java MyScanner <inputfile>");
		} else {
			for (int i = 0; i < argv.length; i++) {
				String fileName = null;
				try {

					fileName = argv[i];
					Parser parser = new Parser(new Scanner(new java.io.FileReader(fileName)), fileName);
					AbstractNode abstractTree = parser.parse();
					SymbolTable syms = new SymbolTable();
					
					System.out.println(abstractTree);
					System.out.println(abstractTree.compile(syms));
					System.out.println(syms);

				} catch (java.io.FileNotFoundException e) {
					System.err.println("File not found : \"" + fileName + "\"");
				} catch (ParserException e) {
					System.err.println(e);
				} catch (Exception e) {
					System.err.println("Unexpected exception:");
					e.printStackTrace();
				}
			}
		}
	}
}
