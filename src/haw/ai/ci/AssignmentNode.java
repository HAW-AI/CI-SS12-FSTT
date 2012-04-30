//package haw.ai.ci;
//
//public class AssignmentNode extends AbstractNode {
//
//    private static final long serialVersionUID = 1L;
//    
//    private final IdentNode ident;
//    private final SelectorNode selector;
//    private final ExpressionNode expression;
//    
//    public AssignmentNode(IdentNode ident, SelectorNode selector,ExpressionNode expression) {
//        this.ident = ident;
//        this.selector = selector;
//        this.expression = expression;
//    }
//	@Override
//	protected String toString(int indent) {
//        String str = toString(indent, "AssignmentNode\n");
//        str += ident.toString(indent+1) + "\n";
//        str += selector.toString(indent+1) + "\n";
//        str += expression.toString(indent+1);
//        return str;
//	}
//	
//	// TODO hash equals
//
//}
