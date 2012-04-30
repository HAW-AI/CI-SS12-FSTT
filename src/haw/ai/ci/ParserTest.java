package haw.ai.ci;

import static haw.ai.ci.TokenID.*;

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
    public void testTestLookAhead() {
        Parser p = createParser("id 123");
        
        assertTrue(p.testLookAhead(INT));
        assertTrue(p.test(IDENT));
        
        p.read();
        
        assertTrue(p.test(INT));
        
    }
}
