package haw.ai.ci;

public class TypeDeclarationNode extends AbstractNode {

	
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode ident;
    private final AbstractNode type;
    
    TypeDeclarationNode(AbstractNode ident,AbstractNode type){
    	this.ident = ident;
    	this.type = type;
    }
	
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "TypeDeclarationNode\n");
        if(ident != null)
        	str += ident.toString(indent+1) + "\n";
            if(type != null)
        	str += type.toString(indent+1) + "\n";
        return str;
	}

}
