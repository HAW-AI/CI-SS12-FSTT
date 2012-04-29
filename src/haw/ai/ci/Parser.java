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
    
    public static void error(String str) {
        throw new ParserException("==> Error: " + str);
    }
    
    private void expect(TokenID expectedToken, String expectedString) {
        if (nextSymbol.id() != expectedToken) {
            failExpectation(expectedString);
        }
    }
    
    private void failExpectation(String expectedString) {
        error("expected " + expectedString + " at line " + nextSymbol.line() + ", column " + nextSymbol.column());
    }
    
    private boolean test(TokenID token) {
        return nextSymbol.id() == token;
    }
    
    private void read(TokenID expectedToken, String expectedString) {
        expect(expectedToken, expectedString);
        insymbol();
    }
    
    private Token read() {
        Token curSymbol = nextSymbol;
        insymbol();
        return curSymbol;
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
        expect(IDENT, "identifier");
        return new IdentNode(read().text());
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
