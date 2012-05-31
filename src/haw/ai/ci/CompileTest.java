package haw.ai.ci;

import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IntNode;

import org.junit.Test;


public class CompileTest {
    
	@Test
	public void testAssign(){
		AssignmentNode an = new AssignmentNode(new IdentNode("i"), new IntNode(7));
		SymbolTable st= new SymbolTable();
		st.declare("i", new SimpleTypeDescriptor(Type.INTEGER));
		an.compile(st);
	}
}
