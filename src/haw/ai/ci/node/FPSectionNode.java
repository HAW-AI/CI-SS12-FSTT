package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;

public class FPSectionNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode node;
    private final AbstractNode type;
    private Descriptor typeOfSection;
    public FPSectionNode(AbstractNode identList, AbstractNode type) {
        this.node = identList;
        this.type=type;
        this.typeOfSection =null;
    }

    @Override
    protected String toString(int indent) {
        String str = toString(indent, "FPSectionNode\n");
		if(node != null)
        str += node.toString(indent+1) + "\n";
		if(type != null)
        str += type.toString(indent+1);
		return str;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode()) + ((type == null) ? 0 : type.hashCode());
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
        FPSectionNode other = (FPSectionNode) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        
        return true;
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
    	
    	IdentListNode n = (IdentListNode) node;
    	n.compileParams(table, d);  
    	typeOfSection = d;
    	
    	return null;
    }
    
    public int size(){
    	if(typeOfSection == null) return -1;
    	IdentListNode n = (IdentListNode) node;
    	return n.size() * typeOfSection.size();
    }

}
