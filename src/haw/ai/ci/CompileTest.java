package haw.ai.ci;

import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;
import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.BinOpNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IntNode;

import org.junit.Test;


public class CompileTest {
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
	
	
}
