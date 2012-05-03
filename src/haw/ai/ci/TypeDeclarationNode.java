package haw.ai.ci;

public class TypeDeclarationNode extends AbstractNode {

	
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((ident == null) ? 0 : ident.hashCode());
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	return result;
    }



    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	TypeDeclarationNode other = (TypeDeclarationNode) obj;
	if (ident == null) {
	    if (other.ident != null)
		return false;
	} else if (!ident.equals(other.ident))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }



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
