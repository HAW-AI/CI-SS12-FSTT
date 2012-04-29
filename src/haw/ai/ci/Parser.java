package haw.ai.ci;

import static haw.ai.ci.TokenID.*;

public class Parser {
    private Token nextSymbol;
    private Scanner scanner;
    private String fileName;
    
    public Parser(Scanner scanner, String fileName) {
        this.scanner = scanner;
        this.fileName = fileName;
    }
    
    static void error(String str) {
        throw new ParserException("==> Error: " + str);
    }
    
    void expect(TokenID expectedToken, String expectedString) {
        if (nextSymbol.id() != expectedToken) {
            failExpectation(expectedString);
        }
    }
    
    void failExpectation(String expectedString) {
        error("expected " + expectedString + " at line " + nextSymbol.line() + ", column " + nextSymbol.column());
    }
    
    boolean test(TokenID token) {
        return nextSymbol.id() == token;
    }
    
    Token read(TokenID expectedToken, String expectedString) {
        expect(expectedToken, expectedString);
        return read();
    }
    
    Token read() {
        Token curSymbol = nextSymbol;
        insymbol();
        return curSymbol;
    }
    
    boolean testLookAhead(TokenID token) {
        Token afterNextSymbol = null;
        
        try {
            afterNextSymbol = scanner.yylex();
            scanner.yypushback(1);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found : \"" + fileName + "\"");
        } catch (java.io.IOException e) {
            System.out.println("IO error scanning file \"" + fileName + "\"");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("Unexpected exception:");
            e.printStackTrace();
        }
        
        return afterNextSymbol.id() == token;
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
    
    AbstractNode selector() {
        IdentNode subject = constIdent();
        AbstractNode node = null;
        
        if (test(DOT)) {
            read(DOT, ".");
            node = new IdentSelectorNode(subject, constIdent());
        } else if(test(LBRAC)) {
            read(LBRAC, "[");
            
            // @TODO read full expression
            expect(INT, "expression");
            node = new ExprSelectorNode(subject, new IntNode(Integer.valueOf(read().text())));
            
            read(RBRAC, "]");
        } else {
            failExpectation(". or [");
        }
        
        return node;
    }
    
    // @TODO: parse read and (expression)
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
        } else {
            failExpectation("identifier, integer, string, read or (expression)");
        }
        
        return node;
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
        insymbol();
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
