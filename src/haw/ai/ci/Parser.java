package haw.ai.ci;

import static haw.ai.ci.TokenID.*;
import static haw.ai.ci.BinOpNode.BinOp.*;

import java.util.ArrayList;
import java.util.LinkedList;
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
     * @param expectedToken  the expected token
     * @param expectedString the expected token in human readable form
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
     * @param expectedString the expected token(s) in human readable form
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
     * @param token the token to test against
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
     * @param expectedToken  the expected token
     * @param expectedString the expected token in human readable form
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
     * @param token the token to test against
     * @return true, if the token after the next token is of the same type as
     *         the provided token, false otherwise
     */
    boolean testLookAhead(TokenID token) {
        Token afterNextSymbol = null;

        try {
            afterNextSymbol = scanner.yylex();
            scanner.yypushback(1);
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
        return new StringNode(str.substring(1, str.length()-1));
    }
    
    AbstractNode indexExpr() {
        AbstractNode node = null;
        
        if (test(INT)) {
            node = integer();
        } else if (test(IDENT)) {
            node = constIdent();
        } else {
            failExpectation("integer or identifier");
        }
        
        return node;
    }
    
    AbstractNode selector() {
        IdentNode subject = constIdent();
        List<AbstractNode> selectors = new LinkedList<AbstractNode>();
        
        while (test(DOT) || test(LBRAC)) {
            if (test(DOT)) {
                read(DOT, ".");
                selectors.add(constIdent());
            } else {
                read(LBRAC, "[");
                selectors.add(indexExpr());
                read(RBRAC, "]");
            }
        }
        
        return new SelectorNode(subject, selectors);
    }
    
////    Assignment = ident Selector �:=� Expression.
//    AssignmentNode assignment() {
//    	AssignmentNode node = null;
//        IdentNode ident = null;
//        SelectorNode selector = null;
//        ExpressionNode expr = null;
//
//        if (test(IDENT)) {
//        	ident = constIdent();
//            if (testLookAhead(DOT)) {
//            	selector = selector();
//                if (test(ASSIGN)) {
//                	read(ASSIGN,":=");
//                	selector = selector();
//                  expr = expression();
//                	node = new AssignmentNode(ident,selector,expr);
//                } else {
//                    failExpectation(":=");
//                }
//            } else {
//                failExpectation(".");
//            }
//        } else {
//            failExpectation("identifier");
//        }
//        return node;
//    }
//    
////    ActualParameters = Expression {�,� Expression}
//    ActualParametersNode actualParameters() {
//    	ArrayList[ExpressionNode] list = new ArrayList[ExpressionNode]();
//        
//		list.add(expression());
//		while (test(COMMA)) {
//        	read(COMMA,",");
//			list.add(expression());
//		}
//		return  new ActualParametersNode(list);
//    }
//    
////    ProcedureCall = ident �(� [ActualParameters] �)�.
//
//    ProcedureCallNode procedureCall() {
//    	IdentNode ident = null;
//    	ActualParametersNode actualParameters = null;
//
//        if (test(IDENT)) {
//        	ident = constIdent();
//        	if(test(LPAR)){
//        		read(LPAR,"(");
//        		actualParameters = actualParameters();
//            	if(test(RPAR)){
//            		read(RPAR,")");
//            	}else{
//                    failExpectation(")");
//            	}
//        	}
//        }
//		return new ProcedureCallNode(ident,actualParameters);
//    }
    
    AbstractNode factor() {
        AbstractNode node = null;
        
        if (test(IDENT)) {
            if (testLookAhead(DOT) || testLookAhead(LBRAC)) {
                node = selector();
            } else {
                node = constIdent();
            }
        } else if (test(INT)) {
            node = integer();
        } else if (test(STR)) {
            node = string();
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
    
    ProcedureDeclarationNode procedureDeclaration() {
    	//TODO: tests
//    	ProcedureDeclaration	=  ProcedureHeading �;� ProcedureBody ident
    	
    	//TODO: man m�sste sich hier den ident aus procHeading holen, damit man den ident nach procBody auf gleichheit pr�fen kann. readAhead methode? oder hier tats�chlich getter in procHeadingNode ben�tigt?
        AbstractNode procHeadingNode = procedureHeading();
        read(SEMICOLON, ";");
        AbstractNode procBodyNode = procedureBody();
        IdentNode identNode = constIdent();
        
        ProcedureDeclarationNode node = new ProcedureDeclarationNode(procHeadingNode, procBodyNode, identNode);
    	
        return node;
    }
    
    AbstractNode declaration() {
    	//TODO: nodes (evtl nur unn�tige ebene? - alles (auch procedures) innerhalb module reicht aus oder?) und tests
//    	Declarations =	[�CONST�  ident  �=�  Expression  �;� {ident  �=�  Expression  �;�}]
//    					[�TYPE�  ident  �=�  Type  �;� {ident  �=�  Type  �;�}]
//    					[�VAR�  IdentList  �:�  Type  �;� {IdentList  �:�  Type  �;�}]
//    					{ProcedureDeclaration  �;�}
    	
    	if (test(CONST)){
    		read(CONST, "const");
    		constIdent();
    		read(ASSIGN, "=");
    		expr();
    		read(SEMICOLON, ";");
    		while (test(IDENT)){
    			constIdent();
    			read(ASSIGN, "=");
    			expr();
    			read(SEMICOLON, ";");
    		}
    	}
    	else if (test(TYPE)){
    		read(TYPE, "type");
    		constIdent();
    		read(ASSIGN, "=");
    		type();
    		read(SEMICOLON, ";");
    		while (test(TYPE)){
    			constIdent();
    			read(ASSIGN, "=");
    			type();
    			read(SEMICOLON, ";");
    		}
    	}
    	else if (test(VAR)){
    		read(VAR, "var");
    		identList();
    		read(COLON, ":");
    		identList();
    		read(SEMICOLON, ";");
    		while (test(VAR)){
    			identList();
    			read(COLON, ":");
    			type();
    			read(SEMICOLON, ";");
    		}
    	}
    	else if (test(PROCEDURE)){
    		procedureDeclaration();
    		read(SEMICOLON, ";");
    	}
    	else {
    		failExpectation("const, type, var or procedure declaration");
    	}
    	
    	return null;
    }
    
    ModuleNode module() {
    	//TODO: tests
//    	Module =	�MODULE�  ident  �;�  Declarations
//    				�BEGIN�  StatementSequence
//    				�END�  ident  �.�
    	
        read(MODULE, "module");
        IdentNode moduleName = constIdent();
        read(SEMICOLON, "semicolon");
        AbstractNode declaration = declaration();
        read(BEGIN, "begin");
        AbstractNode statementSequence = statementSequence();
        read(END, "end");
        IdentNode moduleEndName = constIdent();
        if (!moduleName.equals(moduleEndName)){
        	failExpectation("identifiers of module and end are supposed to be the same");
        }
        read(DOT, ".");
        
        ModuleNode node = new ModuleNode(moduleName, declaration, statementSequence);
        
    	return node;
    }
    
    AbstractNode statementSequence(){
    	return null;
    }

    AbstractNode identList(){
    	return null;
    }
    AbstractNode arrayType(){
    	return null;
    }
    AbstractNode fieldList(){
    	return null;
    }
    AbstractNode recordType(){
    	return null;
    }
    AbstractNode type(){
    	return null;	
    }
    AbstractNode fpSection(){
    	return null;
    }
    AbstractNode formalParameters(){
    	return null;
    }
    AbstractNode procedureHeading(){
    	return null;
    }
    AbstractNode procedureBody(){
    	return null;
    }

    AbstractNode program() {
        AbstractNode tree = null;
        
        while (nextSymbol != null) {
            expect(INT, "integer");
            tree = new IntNode(Integer.valueOf(nextSymbol.text()));
            insymbol();
        }
        
        // last int
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
                    System.out.println(parser.parse());
                    
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
