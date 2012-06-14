package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;

public class FieldListNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode node;
    private final AbstractNode type;
    public FieldListNode(AbstractNode identList, AbstractNode type) {
        this.node = identList;
        this.type=type;
    }

    @Override
    protected String toString(int indent) {
        String str = toString(indent, "FieldListNode\n");
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
        FieldListNode other = (FieldListNode) obj;
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
    	node.compile(table,d);
    	return null; //schreibt ja nur in die Tabelle--> kein Ruechgabewert
    }

}
