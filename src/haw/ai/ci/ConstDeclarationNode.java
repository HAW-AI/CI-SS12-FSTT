package haw.ai.ci;

public class ConstDeclarationNode extends AbstractNode {

	
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode ident;
    private final AbstractNode expression;
    
    ConstDeclarationNode(AbstractNode ident,AbstractNode expression){
    	this.ident = ident;
    	this.expression = expression;
    }
	
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ConstDeclarationNode\n");
        if(ident != null)
        	str += ident.toString(indent+1) + "\n";
        if(expression != null)
        	str += expression.toString(indent+1) + "\n";
        return str;
	}

}
