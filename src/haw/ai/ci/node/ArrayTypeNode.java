package haw.ai.ci.node;

import haw.ai.ci.CompilerException;
import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.ArrayDescriptor;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.IntConstDescriptor;

public class ArrayTypeNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode node;
    private final AbstractNode type;
    public ArrayTypeNode(AbstractNode expression, AbstractNode type) {
        this.node = expression;
        this.type=type;
    }

    @Override
    protected String toString(int indent) {
        String str = toString(indent, "ArrayTypeNode\n");
		if(node != null)
        	str += node.toString(indent+1) + "\n";
		if(type != null)
	    	str += type.toString(indent+1) + "\n";
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
        ArrayTypeNode other = (ArrayTypeNode) obj;
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
    	int size;
    	if(node instanceof IdentNode){
    		size = ((IntConstDescriptor)table.descriptorFor(((IdentNode)node).getIdentName())).value();
    	}else if(node instanceof ContentNode){
    		size = ((IntConstDescriptor)table.descriptorFor((((IdentNode)((ContentNode)node).getSubject()).getIdentName()))).value();
    	}else{
    		size = ((IntNode)node).getVal();
    	}
    	
    	Descriptor descr = null;
    	if (type instanceof ArrayTypeNode || type instanceof RecordTypeNode) {
    	    descr = type.compile(table);
    	} else if (type instanceof IdentNode) {
    	    String identName = ((IdentNode)type).getIdentName();
    	    descr = table.descriptorFor(identName);
    	    
    	    if (descr == null) {
    	        throw new CompilerException("unknown type: " + identName);
    	    }
    	} else {
            throw new CompilerException("unsupported type: " + type);
    	}
    	
    	return new ArrayDescriptor(size, descr);
    }

}
