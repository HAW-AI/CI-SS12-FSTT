package haw.ai.ci;

import static haw.ai.ci.TokenID.ASSIGN;
import static haw.ai.ci.TokenID.IDENT;
import static haw.ai.ci.TokenID.INT;
import static haw.ai.ci.node.BinOpNode.BinOp.DIV_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.EQ_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.HI_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.LO_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.MINUS_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.MUL_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.NEQ_OP;
import static haw.ai.ci.node.BinOpNode.BinOp.PLUS_OP;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import haw.ai.ci.node.AbstractNode;
import haw.ai.ci.node.ActualParametersNode;
import haw.ai.ci.node.ArraySelectorNode;
import haw.ai.ci.node.ArrayTypeNode;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.BinOpNode;
import haw.ai.ci.node.ConstDeclarationNode;
import haw.ai.ci.node.ContentNode;
import haw.ai.ci.node.DeclarationsNode;
import haw.ai.ci.node.FPSectionNode;
import haw.ai.ci.node.FieldListNode;
import haw.ai.ci.node.FormalParametersNode;
import haw.ai.ci.node.IdentListNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IfStatementNode;
import haw.ai.ci.node.IntNode;
import haw.ai.ci.node.ModuleNode;
import haw.ai.ci.node.NegationNode;
import haw.ai.ci.node.PrintNode;
import haw.ai.ci.node.ProcedureCallNode;
import haw.ai.ci.node.ProcedureNode;
import haw.ai.ci.node.ReadNode;
import haw.ai.ci.node.RecordSelectorNode;
import haw.ai.ci.node.RecordTypeNode;
import haw.ai.ci.node.RepeatStatementNode;
import haw.ai.ci.node.StatementSequenceNode;
import haw.ai.ci.node.StringNode;
import haw.ai.ci.node.TypeDeclarationNode;
import haw.ai.ci.node.VarDeclarationNode;
import haw.ai.ci.node.WhileStatementNode;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ParserTest {
    private Parser createParser(String code) {
	Scanner scanner = new Scanner(new StringReader(code));
        return new Parser(scanner, "test");
    }

    @Test(expected = ParserException.class)
    public void testProgramNeg() {
        createParser(
                "MODULE m1; BEGIN a:=b; END m1. MODULE m2; BEGIN a:=b; END m2.")
                .program();
    }

    @Test
    public void testConstIdent() {
        AbstractNode actual = createParser("varn3ame_").constIdent();
        AbstractNode expected = new IdentNode("varn3ame_");
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testConstIdentNeg1() {
        createParser("1varname").constIdent();
    }

    @Test(expected = ParserException.class)
    public void testConstIdentNeg2() {
        createParser("_varname").constIdent();
    }
    
    @Test
    public void testPrint() {
        AbstractNode actual, expected;

        actual = createParser("PRINT 1+2").statement();
        expected = new PrintNode(createParser("1+2").expr());
        assertEquals(expected, actual);
    }

    @Test
    public void testRead() {
        AbstractNode actual, expected;

        actual = createParser("READ").readParser();
        expected = new ReadNode();
        assertEquals(expected, actual);

        actual = createParser("read \"format C:? (y/n)\"").readParser();
        expected = new ReadNode(new StringNode("format C:? (y/n)"));
        assertEquals(expected, actual);
    }

    @Test
    public void testIndexExpr() {
        AbstractNode actual, expected;

        actual = createParser("-123").indexExpr();
        expected = new IntNode(-123);
        assertEquals(expected, actual);

        actual = createParser("v_arname1").indexExpr();
        expected = new ContentNode(new IdentNode("v_arname1"));
        assertEquals(expected, actual);
    }

    @Test
    public void testSelector() {
        AbstractNode actual, expected;

        actual = createParser("ident1.ident2").selector();
        expected = new RecordSelectorNode(new IdentNode("ident1"), new IdentNode(
                "ident2"));
        assertEquals(expected, actual);

        actual = createParser("ident1[-1337]").selector();
        expected = new ArraySelectorNode(new IdentNode("ident1"), new IntNode(-1337));
        assertEquals(expected, actual);

        actual = createParser("ident1[-1337].sel[1]").selector();
        expected = new ArraySelectorNode(new RecordSelectorNode(new ArraySelectorNode(
                new IdentNode("ident1"), new IntNode(-1337)), new IdentNode(
                "sel")), new IntNode(1));
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testSelectorNeg1() {
        createParser("1ident1.ident2").selector();
    }

    @Test(expected = ParserException.class)
    public void testSelectorNeg2() {
        createParser("ident1.4").selector();
    }

//    @Test(expected = ParserException.class)
//    public void testSelectorNeg3() {
//        createParser("ident1[\"foo\" >= -0]").selector();
//    }

    @Test
    public void testFactor() {
        AbstractNode actual, expected;

        actual = createParser("read \"wazup?\"").factor();
        expected = new ReadNode(new StringNode("wazup?"));
        assertEquals(expected, actual);

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
        expected = new BinOpNode(NEQ_OP,
                                 new ContentNode(new IdentNode("a")),
                                 new ContentNode(new IdentNode("b")));
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
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
                new ContentNode(new ArraySelectorNode(new IdentNode("ident"), new IntNode(7))));
        assertEquals(expected, actual);

        actual = createParser("\"hello wOrld\" / ident[7] *   9").term();
        expected = new BinOpNode(MUL_OP, new BinOpNode(DIV_OP, new StringNode(
                "hello wOrld"), new ContentNode(new ArraySelectorNode(new IdentNode("ident"),
                new IntNode(7)))), new IntNode(9));
        assertEquals(expected, actual);

    }

    @Test
    public void testSimpleExpr() {
        AbstractNode actual, expected;

        actual = createParser("-1337*7+\"erna\"").simpleExpr();
        expected = new BinOpNode(PLUS_OP, new BinOpNode(MUL_OP, new IntNode(
                -1337), new IntNode(7)), new StringNode("erna"));
        assertEquals(expected, actual);

        actual = createParser("-\"foo\"").simpleExpr();
        expected = new NegationNode(new StringNode("foo"));
        assertEquals(expected, actual);

        actual = createParser("a-b+c").simpleExpr();
        expected = new BinOpNode(PLUS_OP,
                                 new BinOpNode(MINUS_OP,
                                              new ContentNode(new IdentNode("a")),
                                              new ContentNode(new IdentNode("b"))),
                                              new ContentNode(new IdentNode("c")));
        assertEquals(expected, actual);
    }

    @Test
    public void testExpression() {
        AbstractNode actual, expected;

        actual = createParser("1").expr();
        expected = new IntNode(1);
        assertEquals(expected, actual);

        actual = createParser("a # b").expr();
        expected = new BinOpNode(NEQ_OP,
                                 new ContentNode(new IdentNode("a")),
                                 new ContentNode(new IdentNode("b")));
        assertEquals(expected, actual);

        actual = createParser("-\"foo\" + 9 * blub < 42").expr();
        expected = new BinOpNode(LO_OP, new BinOpNode(PLUS_OP,
                new NegationNode(new StringNode("foo")), new BinOpNode(MUL_OP,
                        new IntNode(9), new ContentNode(new IdentNode("blub")))),
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

//    @Test
//    public void testProcedureDeclaration() {
//    ProcedureDeclarationNode actual, expected;
//
//    actual = createParser("procedure foo(); begin bar := 101 end foo")
//        .procedureDeclaration();
//    List<AssignmentNode> assignment = new ArrayList<AssignmentNode>();
//    assignment.add(new AssignmentNode(new IdentNode("bar"),
//        new IntNode(101)));
//    expected = new ProcedureDeclarationNode(new ProcedureHeadingNode(
//        new IdentNode("foo"), null), new ProcedureBodyNode(
//        new DeclarationsNode(new ArrayList<AbstractNode>(),
//            new ArrayList<AbstractNode>(),
//            new ArrayList<AbstractNode>(),
//            new ArrayList<AbstractNode>()),
//        new StatementSequenceNode(assignment)), new IdentNode("foo"));
//    assertEquals(expected, actual);
//
//    // procedure ohne inhalt und expected anders aufgebaut -
//    // nicht h�ndisch sondern per createParser("").<parsesMethode>()
//    actual = createParser("procedure foo(); begin end foo")
//        .procedureDeclaration();
//    expected = new ProcedureDeclarationNode(new ProcedureHeadingNode(
//        new IdentNode("foo"), null), new ProcedureBodyNode(
//        createParser("").declaration(), createParser("")
//            .statementSequence()), new IdentNode("foo"));
//    assertEquals(expected, actual);
//    }
//
//    @Test(expected = ParserException.class)
//    public void testProcedureDeclarationNeg1() {
//    createParser("procedure foo; begin bar := 101 end foo")
//        .procedureDeclaration();
//    }
//
//    @Test(expected = ParserException.class)
//    public void testProcedureDeclarationNeg2() {
//    createParser("procedure foo(); begin bar := 101 end")
//        .procedureDeclaration();
//    }


    @Test
    public void testDeclaration() {
        DeclarationsNode actual, expected;

        actual = createParser("var x, y : integer;").declaration();
        expected = new DeclarationsNode(new ArrayList<ConstDeclarationNode>(),
                new ArrayList<TypeDeclarationNode>(),
                asList(new VarDeclarationNode(new IdentListNode(asList(
                        new IdentNode("x"), new IdentNode("y"))),
                        new IdentNode("integer"))),
                new ArrayList<ProcedureNode>());
        assertEquals(expected, actual);

        // leere declaration
        actual = createParser("").declaration();
        expected = new DeclarationsNode(new ArrayList<AbstractNode>(),
                new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(),
                new ArrayList<AbstractNode>());
        assertEquals(expected, actual);
        
        actual = createParser("var x, y : integer; z : string;").declaration();
        expected = new DeclarationsNode(new ArrayList<ConstDeclarationNode>(),
                new ArrayList<TypeDeclarationNode>(),
                asList(new VarDeclarationNode(new IdentListNode(asList(
                        new IdentNode("x"), new IdentNode("y"))),
                        new IdentNode("integer")),
                        new VarDeclarationNode(new IdentListNode(asList(
                        new IdentNode("z"))),
                        new IdentNode("string"))),
                new ArrayList<ProcedureNode>());
        assertEquals(expected, actual);
        
        // konstanten
        actual = createParser("const c1 = 10; c2 = 20;").declaration();
		expected = new DeclarationsNode(
				asList(new ConstDeclarationNode(new IdentNode("c1"),
						new IntNode(10)), new ConstDeclarationNode(
						new IdentNode("c2"), new IntNode(20))),
				new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(),
				new ArrayList<AbstractNode>());
        assertEquals(expected, actual);
        //type declaration
        
        actual = createParser("type t1 = array[10] of integer;"+
						       " t2 = record a : t1; end;").declaration();
		expected = new DeclarationsNode(
				new ArrayList<AbstractNode>(),
				asList(new TypeDeclarationNode(new IdentNode("t1"),
						createParser("array[10] of integer").arrayType()),
					   new TypeDeclarationNode(new IdentNode("t2"),
						createParser("record a : t1; end;").recordType())
				),
				new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>());
        assertEquals(expected, actual);
        
        
             
    }

    @Test(expected = ParserException.class)
    public void testDeclarationNeg1() {
        createParser("var x y : integer;").declaration();
    }

    @Test
    public void testModule() {
        ModuleNode actual, expected;
        actual = createParser(
                "module foo; var x : integer; begin x := 101 end foo.")
                .module();
        List<AssignmentNode> assignment = new ArrayList<AssignmentNode>();
        assignment
                .add(new AssignmentNode(new IdentNode("x"), new IntNode(101)));
        expected = new ModuleNode(new IdentNode("foo"), createParser(
                "var x : integer;").declaration(), new StatementSequenceNode(
                assignment));
        assertEquals(expected, actual);

        // module ohne inhalt
        actual = createParser("module foo; begin end foo.").module();
        expected = new ModuleNode(new IdentNode("foo"), createParser("")
                .declaration(), new StatementSequenceNode(
                new ArrayList<AbstractNode>()));
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testModuleNeg1() {
        createParser("module foo; begin end bar.").module();
    }

    @Test
    public void assignment() {
        AbstractNode actual, expected;
        actual = createParser("ident1.kp:=10").assignment();
        expected = new AssignmentNode(createParser("ident1.kp").selector(),
                                      new IntNode(10));
        assertEquals(expected, actual);

        actual = createParser("ident1.kp[1].lol:=10").assignment();
        expected = new AssignmentNode(createParser("ident1.kp[1].lol").selector(),
                                      new IntNode(10));
        assertEquals(expected, actual);

        actual = createParser("ident1[0]:=10").assignment();
        expected = new AssignmentNode(createParser("ident1[0]").selector(),
                                      new IntNode(10));
        assertEquals(expected, actual);
    }

    @Test
    public void actualParameters() {
        AbstractNode actual, expected;
        actual = createParser("10,10,10+10").actualParameters();
        expected = new ActualParametersNode(asList(new IntNode(10),
                new IntNode(10), new BinOpNode(PLUS_OP, new IntNode(10),
                        new IntNode(10))));
        assertEquals(expected, actual);

    }

    @Test
    public void procedureCall() {
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10)").procedureCall();
        expected = new ProcedureCallNode(new IdentNode("callMe"),
                new ActualParametersNode(asList(new IntNode(10),
                        new IntNode(10), new BinOpNode(PLUS_OP,
                                new IntNode(10), new IntNode(10)))));
        assertEquals(expected, actual);

    }

    @Test
    public void statement() {
        AbstractNode actual, expected;
        actual = createParser("callMe(10,10,10+10)").statement();
        expected = new ProcedureCallNode(new IdentNode("callMe"),
                new ActualParametersNode(asList(new IntNode(10),
                        new IntNode(10), new BinOpNode(PLUS_OP,
                                new IntNode(10), new IntNode(10)))));
        assertEquals(expected, actual);

        actual = createParser("").statement();
        expected = null;
        assertEquals(expected, actual);

        
        actual = createParser("a[b] := 1").statement();
        expected = new AssignmentNode(createParser("a[b]").selector(), new IntNode(1));
        assertEquals(expected, actual);
    }

    @Test
    public void statementSequence() {
        AbstractNode actual, expected;

        actual = createParser("callMe(10,10,10+10);callMe(10,10,10+10) END")
                .statementSequence();
        expected = new StatementSequenceNode(asList(
                new ProcedureCallNode(new IdentNode("callMe"),
                        new ActualParametersNode(asList(new IntNode(10),
                                new IntNode(10), new BinOpNode(PLUS_OP,
                                        new IntNode(10), new IntNode(10))))),
                new ProcedureCallNode(new IdentNode("callMe"),
                        new ActualParametersNode(asList(new IntNode(10),
                                new IntNode(10), new BinOpNode(PLUS_OP,
                                        new IntNode(10), new IntNode(10)))))));
        assertEquals(expected, actual);

        actual = createParser("").statementSequence();
        expected = new StatementSequenceNode(new ArrayList<AbstractNode>());
        assertEquals(expected, actual);

        actual = createParser(";").statementSequence();
        expected = new StatementSequenceNode(new ArrayList<AbstractNode>());
        assertEquals(expected, actual);

        actual = createParser("PRINT 1").statementSequence();
        expected = new StatementSequenceNode(asList(createParser("PRINT 1")
                .statement()));
        assertEquals(expected, actual);
    }

    @Test
    public void ifStatement() {
        AbstractNode actual, expected;
        actual = createParser(
                "IF 10 > 5 THEN ident1.kp:=10;PRINT h1 ELSIF 3 < 4 THEN ident1.kp:=10;PRINT h1 ELSE ident1.kp:=10 END")
                .ifStatement();
        expected = new IfStatementNode(
                new BinOpNode(HI_OP, new IntNode(10), new IntNode(5)),
                new StatementSequenceNode(asList(new AssignmentNode(
                        createParser("ident1.kp").selector(), new IntNode(10)),
                        new PrintNode(new ContentNode(new IdentNode("h1"))))),
                new IfStatementNode(new BinOpNode(LO_OP, new IntNode(3),
                        new IntNode(4)), new StatementSequenceNode(asList(
                        new AssignmentNode(createParser("ident1.kp").selector(),
                                new IntNode(10)), new PrintNode(new ContentNode(new IdentNode("h1"))))), null,
                        null),
                new StatementSequenceNode(asList(new AssignmentNode(
                        createParser("ident1.kp").selector(), new IntNode(10)))));
        assertEquals(expected, actual);

        actual = createParser(
                "IF 10 > 5 THEN ident1.kp:=10 ELSIF 3 < 4 THEN ident1.kp:=10 ELSE ident1.kp:=10 END")
                .ifStatement();
        expected = new IfStatementNode(
                new BinOpNode(HI_OP, new IntNode(10), new IntNode(5)),
                createParser("ident1.kp:=10").statementSequence(),
                new IfStatementNode(new BinOpNode(LO_OP, new IntNode(3),
                        new IntNode(4)), createParser("ident1.kp:=10").statementSequence(), null, null),
                        createParser("ident1.kp:=10").statementSequence());
        assertEquals(expected, actual);

        actual = createParser(
                "IF 10 > 5 THEN ident1.kp:=10 ELSE ident1.kp:=10 END")
                .ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),
                new IntNode(5)), createParser("ident1.kp:=10")
                .statementSequence(), null, createParser("ident1.kp:=10")
                .statementSequence());
        assertEquals(expected, actual);

        actual = createParser("IF 10 > 5 THEN ident1.kp:=10 END").ifStatement();
        expected = new IfStatementNode(new BinOpNode(HI_OP, new IntNode(10),
                new IntNode(5)), createParser("ident1.kp:=10").statementSequence(), null, null);
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void ifStatementNeg1() {
        createParser("ELSIF 1 < 2 THEN a:=b; END").ifStatement();
    }

    @Test
    public void repeatStatement() {
        AbstractNode actual, expected;
        actual = createParser("repeat  k := i + 1;  Print k;  i := i + 1; until i = 5; ").repeatStatement();
        expected = new RepeatStatementNode(createParser("k := i + 1;  Print k;  i := i + 1;").statementSequence(),
        		new BinOpNode(EQ_OP, new ContentNode(new IdentNode("i")), new IntNode(5)));
        assertEquals(expected, actual);
    }

    @Test
    public void whileStatement() {
        AbstractNode actual, expected;
        actual = createParser("WHILE 3 < 4 DO ident1.kp:=10;PRINT h1 END")
                .whileStatement();
        expected = new WhileStatementNode(new BinOpNode(LO_OP, new IntNode(3),
                new IntNode(4)), createParser("ident1.kp:=10;PRINT h1").statementSequence());
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testFailExpectationNeg1() {
        // verify that no NullPointerException is thrown when the scanner
        // output is empty
        createParser("").failExpectation("test");
    }

    @Test
    public void testIdentList() {
        AbstractNode actual = createParser("varname1, varname2, varname3")
                .identList();
        List<IdentNode> idents = new ArrayList<IdentNode>();
        idents.add(new IdentNode("varname1"));
        idents.add(new IdentNode("varname2"));
        idents.add(new IdentNode("varname3"));
        AbstractNode expected = new IdentListNode(idents);
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testIdentListNeg() {
        createParser("varname1, 2varname, varname3").identList();
    }

    @Test(expected = ParserException.class)
    public void testArrayTypeNeg() {
        createParser("ARRAY2] OF typename").arrayType();
    }

    @Test(expected = ParserException.class)
    public void testArrayTypeNeg2() {
        createParser("ARRAY[] OF typename").arrayType();
    }

    @Test(expected = ParserException.class)
    public void testArrayTypeNeg3() {
        createParser("ARRAY[2] OF 1234").arrayType();
    }

    @Test(expected = ParserException.class)
    public void testArrayTypeNeg4() {
        createParser("ARRAY[2 OF typename").arrayType();
    }

    @Test
    public void testArrayType() {
        AbstractNode actual = createParser("ARRAY[2] OF typename").arrayType();
        AbstractNode expected = new ArrayTypeNode(new IntNode(2),
                new IdentNode("typename"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFieldList() {
        AbstractNode actual = createParser(
                "varname1, varname2, varname3 : typename").fieldList();
        List<IdentNode> idents = new ArrayList<IdentNode>();
        idents.add(new IdentNode("varname1"));
        idents.add(new IdentNode("varname2"));
        idents.add(new IdentNode("varname3"));
        AbstractNode expected = new FieldListNode(new IdentListNode(idents),
                new IdentNode("typename"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFieldList2() {
        AbstractNode actual = createParser(";").fieldList();
        assertEquals(null, actual);
    }

    @Test(expected = ParserException.class)
    public void testFieldListNeg() {
        createParser("varname1, varname2, varname3 : 123").fieldList();
    }

    @Test(expected = ParserException.class)
    public void testFieldListNeg2() {
        createParser("varname1, varname2, varname3  typename").fieldList();
    }

    @Test
    public void testRecordType() {
        AbstractNode actual = createParser(
                "RECORD varname1, varname2 : integer; varname3, varname4 : boolean END")
                .recordType();
        List<IdentNode> idents = new ArrayList<IdentNode>();
        idents.add(new IdentNode("varname1"));
        idents.add(new IdentNode("varname2"));
        FieldListNode fl1 = new FieldListNode(new IdentListNode(idents),
                new IdentNode("integer"));
        List<IdentNode> idents2 = new ArrayList<IdentNode>();
        idents2.add(new IdentNode("varname3"));
        idents2.add(new IdentNode("varname4"));
        FieldListNode fl2 = new FieldListNode(new IdentListNode(idents2),
                new IdentNode("boolean"));
        List<FieldListNode> fieldLists = new ArrayList<FieldListNode>();
        fieldLists.add(fl1);
        fieldLists.add(fl2);
        AbstractNode expected = new RecordTypeNode(fieldLists);
        assertEquals(expected, actual);
    }

    @Test
    public void testRecordType2() {
        AbstractNode actual = createParser(
                "RECORD ; varname3, varname4 : boolean END").recordType();
        List<IdentNode> idents2 = new ArrayList<IdentNode>();
        idents2.add(new IdentNode("varname3"));
        idents2.add(new IdentNode("varname4"));
        FieldListNode fl2 = new FieldListNode(new IdentListNode(idents2),
                new IdentNode("boolean"));
        List<FieldListNode> fieldLists = new ArrayList<FieldListNode>();
        fieldLists.add(fl2);
        AbstractNode expected = new RecordTypeNode(fieldLists);
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testRecordTypeNeg() {
        createParser("RECORD ; varname3, varname4 : boolean ").recordType();
    }

    @Test(expected = ParserException.class)
    public void testRecordTypeNeg2() {
        createParser(" ; varname3, varname4 : boolean END").recordType();
    }

    @Test(expected = ParserException.class)
    public void testRecordTypeNeg3() {
        createParser("RECORD varname3, varname4 :  END").recordType();
    }

    @Test
    public void testType() {
        AbstractNode actual = createParser("typename").type();
        assertEquals(new IdentNode("typename"), actual);
    }

    @Test
    public void testType2() {
        AbstractNode actual = createParser("RECORD varname : boolean END")
                .type();
        List<IdentNode> idents2 = new ArrayList<IdentNode>();
        idents2.add(new IdentNode("varname"));
        FieldListNode fl2 = new FieldListNode(new IdentListNode(idents2),
                new IdentNode("boolean"));
        List<FieldListNode> fieldLists = new ArrayList<FieldListNode>();
        fieldLists.add(fl2);
        AbstractNode expected = new RecordTypeNode(fieldLists);
        assertEquals(expected, actual);
    }

    @Test
    public void testType3() {
        AbstractNode actual = createParser("ARRAY[2] OF boolean").type();
        AbstractNode expected = new ArrayTypeNode(new IntNode(2),
                new IdentNode("boolean"));
        assertEquals(expected, actual);
    }

    @Test(expected = ParserException.class)
    public void testTypeNeg() {
        createParser("123").type();
    }

    @Test
    public void testFPSection() {
        AbstractNode actual = createParser("VAR varname1, varname2 : boolean")
                .fpSection();
        IdentListNode identList = (IdentListNode) createParser(
                "varname1, varname2").identList();
        assertEquals(new FPSectionNode(identList, new IdentNode("boolean")),
                actual);
    }

    @Test
    public void testFPSection2() {
        AbstractNode actual = createParser("varname1, varname2 : boolean")
                .fpSection();
        IdentListNode identList = (IdentListNode) createParser(
                "varname1, varname2").identList();
        assertEquals(new FPSectionNode(identList, new IdentNode("boolean")),
                actual);
    }

    @Test(expected = ParserException.class)
    public void testFPSectionNeg() {
        createParser("varname1, varname2 : ").fpSection();
    }

    @Test(expected = ParserException.class)
    public void testFPSectionNeg2() {
        createParser("VAR varname1  boolean").fpSection();
    }

    @Test(expected = ParserException.class)
    public void testFPSectionNeg3() {
        createParser("VAR : boolean").fpSection();
    }

    @Test
    public void testFormalParams() {
        AbstractNode actual = createParser(
                "VAR varname: boolean; VAR varname2: integer")
                .formalParameters();
        List<FPSectionNode> fpsections = new ArrayList<FPSectionNode>();
        fpsections.add(createParser("VAR varname: boolean").fpSection());
        fpsections.add(createParser("VAR varname2: integer").fpSection());
        assertEquals(new FormalParametersNode(fpsections), actual);
    }

    @Test(expected = ParserException.class)
    public void testFormalParamsNeg() {
        createParser("VAR varname: ; VAR varname2: integer").formalParameters();
    }

    @Test
    public void testProcedure() {
    ProcedureNode actual, expected;

    actual = createParser("procedure foo(); begin bar := 101 end foo").procedure();
    List<AssignmentNode> assignment = new ArrayList<AssignmentNode>();
    assignment.add(new AssignmentNode(new IdentNode("bar"),
        new IntNode(101)));
    expected = new ProcedureNode(new IdentNode("foo"), new IdentNode("foo"), new FormalParametersNode(new ArrayList<FPSectionNode>()), new DeclarationsNode(new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>()), new StatementSequenceNode(assignment));
    assertEquals(expected, actual);

    // procedure ohne inhalt und expected anders aufgebaut -
    // nicht h�ndisch sondern per createParser("").<parsesMethode>()
    actual = createParser("procedure foo(); begin end foo").procedure();
    expected = new ProcedureNode(new IdentNode("foo"), new IdentNode("foo"), new FormalParametersNode(new ArrayList<FPSectionNode>()), createParser("").declaration(), createParser("").statementSequence());
    assertEquals(expected, actual);
    
	// und alter �bernommener (an neue procedure methode angepasster) proc heading test
    actual = createParser("PROCEDURE mytest (VAR varname: boolean; VAR varname2: integer); begin end mytest").procedure();
    FormalParametersNode fp = createParser("VAR varname: boolean; VAR varname2: integer").formalParameters();
    expected = new ProcedureNode(new IdentNode("mytest"), new IdentNode("mytest"), fp, new DeclarationsNode(new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>(), new ArrayList<AbstractNode>()), new StatementSequenceNode(new ArrayList<AbstractNode>()));

    // null f�r formalparameters nicht erlaubt
    actual = createParser("procedure foo(); begin end foo").procedure();
   	expected = new ProcedureNode(new IdentNode("foo"), new IdentNode("foo"), null, createParser("").declaration(), createParser("").statementSequence());
   	assertFalse(actual.equals(true));
    }

    @Test(expected = ParserException.class)
    public void testProcedureNeg1() {
    createParser("procedure foo; begin bar := 101 end foo")
        .procedure();
    }

    @Test(expected = ParserException.class)
    public void testProcedureNeg2() {
    createParser("procedure foo(); begin bar := 101 end")
        .procedure();
    }
    
    // alte proc heading neg tests �bernommen
    @Test(expected = ParserException.class)
    public void testProcedureNeg3() {
    	createParser("PROCEDURE  (VAR varname: boolean; VAR varname2: integer)")
        .procedure();
    }
    
    // alte proc heading neg tests �bernommen
    @Test(expected = ParserException.class)
    public void testProcedureNeg4() {
    	createParser("PROCEDURE mytest ")
        .procedure();
    }
    
    // formalparameters parser darf nicht aufgerufen werden wenn keine vorhanden sind
    @Test(expected = ParserException.class)
    public void testProcedureNeg5() {
    	ProcedureNode pn = new ProcedureNode(new IdentNode("foo"), new IdentNode("foo"), createParser("").formalParameters(), createParser("").declaration(), createParser("").statementSequence());
    }

}
