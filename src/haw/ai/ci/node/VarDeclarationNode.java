package haw.ai.ci.node;

import haw.ai.ci.CompilerException;
import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;

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
    
    private final IdentListNode identList;
    private final AbstractNode type;
    
    public VarDeclarationNode(AbstractNode identList, AbstractNode type){
    	this.identList = (IdentListNode)identList;
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

	@Override
	public int size() {
		return identList.size();
	}
	
	public Descriptor compile(SymbolTable table){
		Descriptor d = null;
		if(type instanceof IdentNode){
			String s = ((IdentNode) type).getIdentName();
			if(s.equals("integer")){
				d = new SimpleTypeDescriptor(Type.INTEGER);
			}
			else if(s.equals("boolean")){
				d = new SimpleTypeDescriptor(Type.BOOLEAN);
			}
			else if(s.equals("string")){
				d = new SimpleTypeDescriptor(Type.STRING);
			}
			else{
				d = table.descriptorFor(s);
			}
		}
		else{
			d = type.compile(table);
		}
		identList.compile(table, d);
		return d;
	}

}
