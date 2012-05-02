package haw.ai.ci;

public class VarDeclarationNode extends AbstractNode{

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode identList;
    private final AbstractNode type;
    
    VarDeclarationNode(AbstractNode identList,AbstractNode type){
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
