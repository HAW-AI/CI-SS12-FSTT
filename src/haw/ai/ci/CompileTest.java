package haw.ai.ci;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.io.StringReader;

import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;
import haw.ai.ci.node.AbstractNode;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.BinOpNode;
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
		System.out.println(declarationsData);
		SymbolTable expected = new SymbolTable();
		expected.declare("a", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("b", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("c", new SimpleTypeDescriptor(Type.INTEGER));
		expected.declare("x", new SimpleTypeDescriptor(Type.BOOLEAN));
		
		SymbolTable actual = new SymbolTable();
		declarationsData.compile(actual);
		
		assertEquals(expected,actual);
	}
	
	
}
