package haw.ai.ci;

import static haw.ai.ci.TokenID.*;

public class Parser {
    private Token nextSymbol;
    private MyScanner scanner;
    private String fileName;
    
    public Parser(MyScanner scanner, String fileName) {
        this.scanner = scanner;
        this.fileName = fileName;
    }
    
    public static void error(String str) {
        System.out.println("==> Error: " + str);
        System.exit(0);
    }
    
    private void expect(TokenID expectedToken, String expectedString) {
        if (nextSymbol.id() != expectedToken) {
            error("expected " + expectedString + " at line " + nextSymbol.line() + ", column " + nextSymbol.column());
        }
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
    
    private AbstractNode program() {
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
                    Parser parser = new Parser(new MyScanner(new java.io.FileReader(fileName)), fileName);
                    System.out.println(parser.parse());
                    
                } catch (java.io.FileNotFoundException e) {
                    System.out.println("File not found : \"" + fileName + "\"");
                } catch (Exception e) {
                    System.out.println("Unexpected exception:");
                    e.printStackTrace();
                }
            }
        }
    }
}
