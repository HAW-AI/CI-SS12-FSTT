package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class StringNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final String strVal;
    
    public StringNode(String strVal) {
        this.strVal = strVal;
    }
    
    public String getVal(){
    	return strVal;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "StringNode(" + strVal + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((strVal == null) ? 0 : strVal.hashCode());
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
        StringNode other = (StringNode) obj;
        if (strVal == null) {
            if (other.strVal != null)
                return false;
        } else if (!strVal.equals(other.strVal))
            return false;
        return true;
    }

	@Override
	public Descriptor compile(SymbolTable symbolTable) {
		write("PUSHS, " + strVal);
		return null;
	}
}
