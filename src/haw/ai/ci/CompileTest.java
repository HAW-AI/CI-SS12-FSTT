package haw.ai.ci;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.io.StringReader;

import haw.ai.ci.descriptor.ArrayDescriptor;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.RecordDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;
import haw.ai.ci.node.AbstractNode;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.BinOpNode;
import haw.ai.ci.node.ContentNode;
import haw.ai.ci.node.IdentListNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IfStatementNode;
import haw.ai.ci.node.IntNode;
import haw.ai.ci.node.VarDeclarationNode;

import org.junit.Test;


public class CompileTest {
	private Parser createParser(String code) {
		Scanner scanner = new Scanner(new StringReader(code));
	        return new Parser(scanner, "test");
	    }
    
    private void log(String s){
    	System.out.println(s);
    }

	@Test
	public void testAssign(){
		log("--Assign--");
		log("// a:=7");
		SymbolTable st= new SymbolTable();
		AssignmentNode an = new AssignmentNode(new IdentNode("a"), new IntNode(7));
		st.declare("a", new SimpleTypeDescriptor(Type.INTEGER));
		an.compile(st);
		log("--AssignBinOp--");
		log("// a:=a * b");
		st.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		AssignmentNode an2 = new AssignmentNode(new IdentNode("a"), new BinOpNode(BinOpNode.BinOp.MUL_OP, new IdentNode("a"), new IdentNode("b")));
		an2.compile(st);
		log("// a:=b + 2");
		AssignmentNode an3 = new AssignmentNode(new IdentNode("a"), new BinOpNode(BinOpNode.BinOp.PLUS_OP, new IdentNode("b"), new IntNode(2)));
		an3.compile(st);
		// arrays
//		// siehe arrayselectornode test
		// records
		// siehe recordselectornode test
	}
    
    @Test(expected=CompilerException.class)
    public void testAssignNeg1() {
        // prevent overriding of constants
        String code = "MODULE m; const x = 5; BEGIN x := 99; END m.";
        AbstractNode node = createParser(code).program();
        node.compile(new SymbolTable());
    }
	
	@Test
	public void testIfElse(){
		log("--IfElse--");
		log("// a:=1");
		SymbolTable st= new SymbolTable();
		AssignmentNode an = new AssignmentNode(new IdentNode("a"), new IntNode(1));
		st.declare("a", new SimpleTypeDescriptor(Type.INTEGER));
		an.compile(st);
		log("// b := 2");
		AssignmentNode an2 = new AssignmentNode(new IdentNode("b"), new IntNode(2));
		st.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		an2.compile(st);
		
		log("// if a < b");
		AbstractNode exp = new BinOpNode(BinOpNode.BinOp.LO_OP, new IdentNode("a"), new IdentNode("b"));

		log("// then a := a * b");
		AssignmentNode seq1 = new AssignmentNode(new IdentNode("a"), new BinOpNode(BinOpNode.BinOp.MUL_OP, new IdentNode("a"), new IdentNode("b")));
		
		log("// else if a = b");
		AbstractNode exp2 = new BinOpNode(BinOpNode.BinOp.EQ_OP, new IdentNode("a"), new IdentNode("b"));
		log("// then a := a / b");
		AssignmentNode seq3 = new AssignmentNode(new IdentNode("a"), new BinOpNode(BinOpNode.BinOp.DIV_OP, new IdentNode("a"), new IdentNode("b")));
		
		log("// else a := a * 10");
		AssignmentNode seq2 = new AssignmentNode(new IdentNode("a"), new BinOpNode(BinOpNode.BinOp.MUL_OP, new IdentNode("a"), new IntNode(10)));
		
		new IfStatementNode(exp, seq1, new IfStatementNode(exp2, seq3, null, null), seq2).compile(st);
	}
	
	@Test
	public void testVarDecNode(){
	/*
	 * var a,b,c : integer; 
	 */
		AbstractNode testData = new VarDeclarationNode(new IdentListNode(asList(
                new IdentNode("a"), new IdentNode("b"), new IdentNode("c"))),
                new IdentNode("integer"));
		
		SymbolTable expected = new SymbolTable();
		expected.declare("a", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("c", new SimpleTypeDescriptor(Type.INTEGER));
		SymbolTable actual = new SymbolTable();
		testData.compile(actual);
		
//		System.out.println(expected);
//		System.out.println("\n-----------------------------------\n");
//		System.out.println(actual);
		
		assertEquals(expected, actual);
		
		
	}
	@Test
	public void testDeclarationNode_OnlyVarPart(){
		/*
		 * var a,b,c : integer;
		 *     x : boolean;
		 */
		
		AbstractNode declarationsData = createParser("var a,b,c : integer; x : boolean;").declaration();
//		System.out.println("--------------------declarationsData----------------------");
//		System.out.println(declarationsData);
		SymbolTable expected = new SymbolTable();
		expected.declare("a", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("c", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("x", new SimpleTypeDescriptor(Type.BOOLEAN));
		
		SymbolTable actual = new SymbolTable();
		declarationsData.compile(actual);
		
		assertEquals(expected,actual);
		
		AbstractNode testData = createParser("var a : record " +
				"           s : record"  +
				"                d : integer;" +
				"                z : integer" +
				"                end;" +
				"           z : integer" +
				"           end;" +
				"    b : integer;").declaration();
//		System.out.println("-----------der ParseAst------------------");
//		System.out.println(testData);
		SymbolTable innerInnerTable = new SymbolTable();
		innerInnerTable.declare("d", new SimpleTypeDescriptor(Type.INTEGER));
		innerInnerTable.declare("z", new SimpleTypeDescriptor(Type.INTEGER));
		RecordDescriptor sDescriptor = new RecordDescriptor(innerInnerTable);
		SymbolTable innerTable = new SymbolTable();
		innerTable.declare("s", sDescriptor);
		innerTable.declare("z", new SimpleTypeDescriptor(Type.INTEGER));
		RecordDescriptor aDescriptor = new RecordDescriptor(innerTable);
		expected = new SymbolTable();
		expected.declare("a", aDescriptor);
		expected.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		SymbolTable actual2 = new SymbolTable();
//		System.out.println("------------------------die aufgebaute SymbolTabelle--------------------------");
//		System.out.println(expected);
		testData.compile(actual2);
		assertEquals(expected,actual2);
	}
	
	@Test
	public void testGeschachtelteTypenDeclaration(){
		AbstractNode testData = createParser(
				"var a : ARRAY [10] of RECORD " + //37
				"           s : record"  +  //58
				"                d : integer;" +//86+
				"                z : integer" + //113
				"                end;" + //133
				"           x : integer " + //156
				"           end;" +  //171
				"    b : integer; ").declaration();
		
		SymbolTable tableOfS = new SymbolTable();
		tableOfS.declare("d", new SimpleTypeDescriptor(Type.INTEGER));
		tableOfS.declare("z", new SimpleTypeDescriptor(Type.INTEGER));
		Descriptor sDescriptor = new RecordDescriptor(tableOfS);
		
		SymbolTable tableOfRecord = new SymbolTable();
		tableOfRecord.declare("s", sDescriptor);
		tableOfRecord.declare("x", new SimpleTypeDescriptor(Type.INTEGER));
		Descriptor recordDescr = new RecordDescriptor(tableOfRecord);
		
		SymbolTable expected = new SymbolTable();
		Descriptor outerDescriptor = new ArrayDescriptor(10,recordDescr);
		expected.declare("a", outerDescriptor);
		expected.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		
		SymbolTable actual = new SymbolTable();
		testData.compile(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRepeatUntilNode(){
		log("---------------------------------------RepeatUntil--------------------------------------");
		AbstractNode testData = createParser("MODULE m;" +
				"var repeatVar : integer;" +
				"BEGIN " +
				"repeatVar := 0;"+
				"REPEAT repeatVar := 1 UNTIL 5=5 " +
				"END m.").module();
		testData.compile(new SymbolTable());
	}
	
	@Test
	public void testWhileStatementNode(){
		log("---------------------------------------WhileStatement--------------------------------------");
		AbstractNode testData = createParser("MODULE m;" +
				"var whileVar : integer;" +
				"BEGIN " +
				"whileVar := 3;"+
				"WHILE whileVar < 6 DO whileVar := whileVar + 2 END" +
		" END m.").module();
		testData.compile(new SymbolTable());
	}
	
	@Test
	public void testContentNode(){
		log("---------------------------------------ContentNode--------------------------------------");
	}
	
	@Test
	public void testNegationNode(){
		log("---------------------------------------NegationNode--------------------------------------");
		AbstractNode testData = createParser("Module m; var testVar : integer; begin testVar := -(1+2); end m.").module();
		testData.compile(new SymbolTable());
		
	}
	
	@Test
	public void testRecordSelectorNode(){
		log("-----------------RecordSelectorNode-compile()----------------------");
		AbstractNode testData = createParser(
				"MODULE m;" + 
				"var a, c : record " +
				"           s : record"  +
				"                d : integer;" +
				"                z : integer" +
				"                end;" +
				"           z : integer" +
				"           end;" +
				"    b : integer;" +
				"BEGIN " + 
				"b := 5;" + 
				"a.s.z := 3" + 
				"END m.").module();
		
		testData.compile(new SymbolTable());
		log("----------------------EndOfRecordSelectorNode-compile()---------------");
	}
	
	@Test
	public void testArraySelectorNode(){
		log("-------------------ArraySelectorNode -compile()-------------------");
		AbstractNode testData = createParser(
				"MODULE m;" + 
						"var a : ARRAY [10] of RECORD " + //37
						"           s : record"  +  //58
						"                d : integer;" +//86+
						"                z : integer" + //113
						"                end;" + //133
						"           x : integer " + //156
						"           end;" +  //171
						"    b : integer; " +
				"BEGIN " +  //194 
				"b := 4; " + //202 
				"a[2+3].s.z := 3 " + 
				"END m.").module();
		
		testData.compile(new SymbolTable());
		
		log("--------------------erwartet war----------------------------------");
		log("PUSHI, 4\nPUSHI, 30\nASSIGN, 1\nPUSHI, 3\nPUSHI, 0\nPUSHI, 2\nPUSHI, 3\nADD\nPUSHI, 3\nMUL\nADD\n"+
		    "PUSHI, 0\nADD\nPUSHI, 1\nADD\nASSIGN, 1");
		
		
		

		
	}
	
	
	@Test
	public void testRead() {
        log("-------------------ReadNode-compile()-------------------");
        createParser(
            "MODULE m;\n" +
            "    var age : INTEGER;\n" +
            "BEGIN\n" +
            "    age := READ \"How old are you?\";\n" +
            "    PRINT \"Your age is \";\n" +
            "    PRINT age;\n" +
            "END m."
            ).program().compile(new SymbolTable());
	}
	
	@Test
	public void testProcedureWithoutParamsAndLokals(){
		log("--------------------Procedure1-----------------------------"); 
		AbstractNode prog = createParser(
				"Module m;\n " +
				"VAR a : integer;\n" +
				"PROCEDURE p1();\n" +
				"BEGIN \n" +
				"a := 1 \n" +
				"END p1 \n;" +
				"BEGIN \n" +
				"p1() \n" +
				"END m.").program();
		prog.compile(new SymbolTable());
		
		
	}
}
