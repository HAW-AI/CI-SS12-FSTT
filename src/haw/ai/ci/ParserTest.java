package haw.ai.ci;

import static haw.ai.ci.TokenID.*;
import static haw.ai.ci.BinOpNode.BinOp.*;

import static java.util.Arrays.asList;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.*;

public class ParserTest {
    private Parser createParser(String code) {
        Scanner scanner = new Scanner(new StringReader(code));
        return new Parser(scanner, "test");
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
    public void testIndexExpr() {
        AbstractNode actual, expected;

        actual = createParser("-123").indexExpr();
        expected = new IntNode(-123);
        assertEquals(expected, actual);

        actual = createParser("v_arname1").indexExpr();
        expected = new IdentNode("v_arname1");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSelector() {
        AbstractNode actual, expected;

        actual = createParser("ident1.ident2").selector();
        expected = new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("ident2")));
        assertEquals(expected, actual);

        actual = createParser("ident1[-1337]").selector();
        expected = new SelectorNode(new IdentNode("ident1"), asList(new IntNode(-1337)));
        assertEquals(expected, actual);

        actual = createParser("ident1[-1337].sel[1]").selector();
        expected = new SelectorNode(new IdentNode("ident1"),
                                    asList(new IntNode(-1337),
                                           new IdentNode("sel"),
                                           new IntNode(1)));
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
    
    @Test(expected=ParserException.class)
    public void testSelectorNeg3() {
        createParser("ident1[\"foo\" >= -0]").selector();
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
        
        actual = createParser("(a # b)").factor();
        expected = new BinOpNode(NEQ_OP, new IdentNode("a"), new IdentNode("b"));
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
                                         new SelectorNode(new IdentNode("ident"),
                                                          asList(new IntNode(7))));
        assertEquals(expected, actual);

        actual = createParser("\"hello wOrld\" / ident[7] *   9").term();
        expected = new BinOpNode(MUL_OP, new BinOpNode(DIV_OP, new StringNode("hello wOrld"),
                                                               new SelectorNode(new IdentNode("ident"),
                                                                                asList(new IntNode(7)))),
                                         new IntNode(9));
        assertEquals(expected, actual);

    }

    @Test
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
    public void testExpression() {
        AbstractNode actual, expected;
        
        actual = createParser("1").expr();
        expected = new IntNode(1);
        assertEquals(expected, actual);
        
        actual = createParser("a # b").expr();
        expected = new BinOpNode(NEQ_OP, new IdentNode("a"), new IdentNode("b"));
        assertEquals(expected, actual);
        
        actual = createParser("-\"foo\" + 9 * blub < 42").expr();
        expected = new BinOpNode(LO_OP, new BinOpNode(PLUS_OP, new NegationNode(new StringNode("foo")),
                                                               new BinOpNode(MUL_OP, new IntNode(9),
                                                                                     new IdentNode("blub"))),
                                        new IntNode(42));
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTest() {
        assertTrue(createParser("").test(null));
        assertTrue(createParser("1").test(INT));
    }
    
    @Test
    public void testTestLookAhead() {
        Parser p = createParser("id 123");
        
        assertTrue(p.testLookAhead(INT));
        assertTrue(p.test(IDENT));
        
        p.read();
        
        assertTrue(p.test(INT));
        

        assertTrue(createParser("").testLookAhead(null));
        assertTrue(createParser("1").testLookAhead(null));
        assertFalse(createParser("1 2").testLookAhead(null));
        
        
        p = createParser("a:=b");
        assertTrue(p.testLookAhead(ASSIGN));
        assertTrue(p.testLookAhead(ASSIGN));
    }
    
    
    @Test
    public void assignment() {
        AbstractNode actual, expected;
        actual = createParser("ident1.kp:=10").assignment();
        expected = new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) );
        assertEquals(expected, actual);

        actual = createParser("ident1.kp[1].lol:=10").assignment();
        expected = new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"),new IntNode(1),new IdentNode("lol"))),new IntNode(10) );
        assertEquals(expected, actual);
        
    }
    @Test
    public void actualParameters() {
        AbstractNode actual, expected;
        actual = createParser("10,10,10+10").actualParameters();
        expected = new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) ));
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void procedureCall() {
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10)").procedureCall();
        expected = new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) )));
        assertEquals(expected, actual);
        
    }
    

    @Test
    public void statement() {
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10)").statement();
        expected = new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) )));
        assertEquals(expected, actual);
        
    }
//    @Test
//    public void statementSequenz() {
//        AbstractNode actual, expected;
//        actual = createParser("callMe(10,10,10+10);IF 10 > 5 THEN 4;h1 ELSIF 3 < 4 THEN 6;h1 ELSE 3 END").statementSequenz();
//        expected = new StatementSequenzNode(asList(new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) ))),
//        	new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)),new StatementSequenzNode(asList(new IntNode(4),new IdentNode("h1"))), asList(new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)),new IntNode(6)), new StatementSequenzNode(asList(new IntNode(3),new IdentNode("h1"))))));
//        assertEquals(expected, actual);
//        
//    }
    

//    @Test
//    public void statementSequenz() {
//        AbstractNode actual, expected;
//        actual = createParser("callMe(10,10,10+10);IF 10 > 5 THEN 4 ELSIF 3 < 4 THEN 6 ELSE 3").ifStatement();
//        expected = new StatementSequenzNode(asList(new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) ))),
//        	new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)),new StatementSequenzNode(asList(new IntNode(4))), asList(new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)),new IntNode(6)), new StatementSequenzNode(asList(new IntNode(3))))));
//        assertEquals(expected, actual);
//        
//    }
    
    
//    @Test
//    public void ifStatement() {
//        AbstractNode actual, expected;
//        actual = createParser("IF 10 > 5 THEN ident1.kp:=10;PRINT h1 ELSIF 3 < 4 THEN ident1.kp:=10;PRINT h1 ELSE 3 END").ifStatement();
//        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)),new StatementSequenzNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1"))), asList(new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)), new StatementSequenzNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1")))),new IntNode(6));
//        assertEquals(expected, actual); 
//    }
    
    
    @Test(expected=ParserException.class)
    public void testFailExpectationNeg1() {
        // verify that no NullPointerException is thrown when the scanner
        // output is empty
        createParser("").failExpectation("test");
    }
}
