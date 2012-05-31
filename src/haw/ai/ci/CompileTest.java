package haw.ai.ci;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;
import haw.ai.ci.node.AbstractNode;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.IdentListNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IntNode;
import haw.ai.ci.node.VarDeclarationNode;

import org.junit.Test;


public class CompileTest {
    
	@Test
	public void testAssign(){
		AssignmentNode an = new AssignmentNode(new IdentNode("i"), new IntNode(7));
		SymbolTable st= new SymbolTable();
		st.declare("i", new SimpleTypeDescriptor(Type.INTEGER));
		an.compile(st);
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
		
		System.out.println(expected);
		System.out.println("\n-----------------------------------\n");
		System.out.println(actual);
		
		assertEquals(expected, actual);
		
		
	}
}
