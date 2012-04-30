package haw.ai.ci;

import static haw.ai.ci.TokenID.*;
import static haw.ai.ci.BinOpNode.BinOp.*;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.*;

public class ParserTest {
    private Parser createParser(String code) {
        Scanner scanner = new Scanner(new StringReader(code));
        Parser parser = new Parser(scanner, "test");
        parser.insymbol();
        return parser;
    }
    
    @Test
    public void testProgram() {
        AbstractNode actual = createParser("123\n32 1").program();
        AbstractNode expected = new IntNode(1);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConstIdent() {
        AbstractNode actual = createParser("varn3ame_").constIdent();
        AbstractNode expected = new IdentNode("varn3ame_");
        assertEquals(expected, actual);
    }
    
    @Test(expected=ParserException.class)
    public void testConstIdentNeg1() {
        createParser("1varname").constIdent();
    }
    
    @Test(expected=ParserException.class)
    public void testConstIdentNeg2() {
        createParser("_varname").constIdent();
    }
    
    @Test
    public void testSelector() {
        AbstractNode actual, expected;

        actual = createParser("ident1.ident2").selector();
        expected = new IdentSelectorNode(new IdentNode("ident1"), new IdentNode("ident2"));
        assertEquals(expected, actual);

        actual = createParser("ident1[-1337]").selector();
        expected = new ExprSelectorNode(new IdentNode("ident1"), new IntNode(-1337));
        assertEquals(expected, actual);
    }
    
    @Test(expected=ParserException.class)
    public void testSelectorNeg1() {
        createParser("1ident1.ident2").selector();
    }
    
    @Test(expected=ParserException.class)
    public void testSelectorNeg2() {
        createParser("ident1.4").selector();
    }
    
    @Test
    public void testFactor() {
        AbstractNode actual, expected;
        
        actual = createParser("1337").factor();
        expected = new IntNode(1337);
        assertEquals(expected, actual);
        
        actual = createParser("\"hello wOrld\"").factor();
        expected = new StringNode("hello wOrld");
        assertEquals(expected, actual);
        
        actual = createParser("-1337").factor();
        expected = new IntNode(-1337);
        assertEquals(expected, actual);
    }
    
    @Test(expected=ParserException.class)
    public void testFactorNeg1() {
        createParser("_varname").factor();
    }

    @Test
    public void testTerm() {
        AbstractNode actual, expected;
        
        actual = createParser("1337*7").term();
        expected = new BinOpNode(MUL_OP, new IntNode(1337), new IntNode(7));
        assertEquals(expected, actual);
        
        actual = createParser("\"hello wOrld\" / ident[7]").term();
        expected = new BinOpNode(DIV_OP, new StringNode("hello wOrld"),
                                         new ExprSelectorNode(new IdentNode("ident"),
                                                              new IntNode(7)));
        assertEquals(expected, actual);

//        test later when term() supports sub-expressions        
//        actual = createParser("\"hello wOrld\" / ident[7] *   9").term();
//        expected = new BinOpNode(DIV_OP, new StringNode("hello wOrld"),
//                                         new BinOpNode(MUL_OP, new ExprSelectorNode(new IdentNode("ident"),
//                                                                                    new IntNode(7)),
//                                                               new IntNode(9)));
//        assertEquals(expected, actual);

    }

    @Test(expected=ParserException.class)
    public void testSimpleExpr() {
        AbstractNode actual, expected;
        
        actual = createParser("-1337*7+\"erna\"").simpleExpr();
        expected = new BinOpNode(PLUS_OP, new BinOpNode(MUL_OP, new IntNode(-1337),
                                                                new IntNode(7)),
                                          new StringNode("erna"));
        assertEquals(expected, actual);
        
        actual = createParser("-\"foo\"").simpleExpr();
        expected = new NegationNode(new StringNode("foo"));
        assertEquals(expected, actual);
        
        actual = createParser("a-b+c").simpleExpr();
        expected = new BinOpNode(PLUS_OP, new BinOpNode(MINUS_OP, new IdentNode("a"),
                                                                  new IdentNode("b")),
                                          new IdentNode("c"));
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTestLookAhead() {
        Parser p = createParser("id 123");
        
        assertTrue(p.testLookAhead(INT));
        assertTrue(p.test(IDENT));
        
        p.read();
        
        assertTrue(p.test(INT));
        
    }
}
