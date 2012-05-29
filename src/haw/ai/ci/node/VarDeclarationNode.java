package haw.ai.ci.node;

public class VarDeclarationNode extends AbstractNode{

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((identList == null) ? 0 : identList.hashCode());
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
	VarDeclarationNode other = (VarDeclarationNode) obj;
	if (identList == null) {
	    if (other.identList != null)
		return false;
	} else if (!identList.equals(other.identList))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }



    private static final long serialVersionUID = 1L;
    
    private final AbstractNode identList;
    private final AbstractNode type;
    
    public VarDeclarationNode(AbstractNode identList,AbstractNode type){
    	this.identList = identList;
    	this.type = type;
    }
	
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "VarDeclarationNode\n");
        if(identList != null)
        	str += identList.toString(indent+1) + "\n";
        if(type != null)
        	str += type.toString(indent+1) + "\n";
        return str;
	}

}
