package haw.ai.ci;

import static haw.ai.ci.BinOpNode.BinOp.DIV_OP;
import static haw.ai.ci.BinOpNode.BinOp.HI_OP;
import static haw.ai.ci.BinOpNode.BinOp.LO_OP;
import static haw.ai.ci.BinOpNode.BinOp.MINUS_OP;
import static haw.ai.ci.BinOpNode.BinOp.MUL_OP;
import static haw.ai.ci.BinOpNode.BinOp.NEQ_OP;
import static haw.ai.ci.BinOpNode.BinOp.PLUS_OP;
import static haw.ai.ci.TokenID.ASSIGN;
import static haw.ai.ci.TokenID.IDENT;
import static haw.ai.ci.TokenID.INT;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Test;

public class ParserTest {
    private Parser createParser(String code) {
        Scanner scanner = new Scanner(new StringReader(code));
        return new Parser(scanner, "test");
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
    public void testProcedureDeclaration() {
        ProcedureDeclarationNode actual, expected;
        
        actual = createParser("procedure foo(); begin bar := 42; end foo").procedureDeclaration();
        //TODO: folgender test failt noch, arbeite dran. push für felix. - phil
        expected = new ProcedureDeclarationNode(new ProcedureHeadingNode(new IdentNode("foo"), null), new ProcedureBodyNode(null, null), new IdentNode("foo"));
        assertEquals(expected, actual);
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
    	//TODO: testen: statement darf auch leer sein
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10)").statement();
        expected = new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) )));
        assertEquals(expected, actual);
        
    }
    @Test
    public void statementSequence() {
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10);callMe(10,10,10+10);END").statementSequence();
        expected = new StatementSequenceNode(asList(new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) ))),
        		new ProcedureCallNode(new IdentNode("callMe"), new ActualParametersNode( asList(new IntNode(10),new IntNode(10),new BinOpNode(PLUS_OP, new IntNode(10),new IntNode(10)) )))));
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void ifStatement() {
        AbstractNode actual, expected;
        actual = createParser("IF 10 > 5 THEN ident1.kp:=10;PRINT h1; ELSIF 3 < 4 THEN ident1.kp:=10;PRINT h1; ELSE ident1.kp:=10; END").ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)) , new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1"))) , new IfStatementNode(new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)),new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1"))),null,null) ,  new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10)))));
        assertEquals(expected, actual); 
        
        actual = createParser("IF 10 > 5 THEN ident1.kp:=10; ELSIF 3 < 4 THEN ident1.kp:=10; ELSE ident1.kp:=10; END").ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)) , new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ))) , new IfStatementNode(new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)),new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ))),null,null) ,  new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10)))));
        assertEquals(expected, actual); 
        
        actual = createParser("IF 10 > 5 THEN ident1.kp:=10; ELSE ident1.kp:=10; END").ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)) , new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ))) , null ,  new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10)))));
        assertEquals(expected, actual); 
        
        actual = createParser("IF 10 > 5 THEN ident1.kp:=10; END").ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),new IntNode(5)) , new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ))) ,null ,  null);
        assertEquals(expected, actual); 
    }
    
    @Test(expected=ParserException.class)
    public void ifStatementNeg1() {
        createParser("ELSIF 1 < 2 THEN a:=b; END").ifStatement();
    }
    
    @Test
    public void repeatStatement() {
        AbstractNode actual, expected;
        actual = createParser("REPEAT ident1.kp:=10;PRINT h1; UNTIL 3 < 4").repeatStatement();
        expected = new RepeatStatementNode(new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1"))) , new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)));
        assertEquals(expected, actual); 
    }
    
    @Test
    public void whileStatement() {
        AbstractNode actual, expected;
        actual = createParser("WHILE 3 < 4 DO ident1.kp:=10;PRINT h1; END").whileStatement();
        expected = new WhileStatementNode( new BinOpNode(LO_OP, new IntNode(3),new IntNode(4)),new StatementSequenceNode(asList(new AssignmentNode(new SelectorNode(new IdentNode("ident1"), asList(new IdentNode("kp"))),new IntNode(10) ),new IdentNode("h1"))));
        assertEquals(expected, actual); 
    }
    
    @Test(expected=ParserException.class)
    public void testFailExpectationNeg1() {
        // verify that no NullPointerException is thrown when the scanner
        // output is empty
        createParser("").failExpectation("test");
    }
}
