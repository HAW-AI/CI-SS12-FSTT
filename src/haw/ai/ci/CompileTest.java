package haw.ai.ci;

import haw.ai.ci.node.AssignmentNode;
import haw.ai.ci.node.IdentNode;
import haw.ai.ci.node.IntNode;

import org.junit.Test;


public class CompileTest {

	@Test
	public void testAssign(){
		AssignmentNode an = new AssignmentNode(new IdentNode("i"), new IntNode(7));
	}
}
