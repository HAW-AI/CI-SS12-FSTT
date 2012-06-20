package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class IdentNode extends AbstractNode {
    private static final long serialVersionUID = 1L;

    private final String identName;
    
    public IdentNode(String identName) {
        this.identName = identName;
    }
    
    public String getIdentName(){
    	return this.identName;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "IdentNode(" + identName + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((identName == null) ? 0 : identName.hashCode());
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
        IdentNode other = (IdentNode) obj;
        if (identName == null) {
            if (other.identName != null)
                return false;
        } else if (!identName.equals(other.identName))
            return false;
        return true;
    }
	@Override
	public Descriptor compile(SymbolTable symbolTable) {
		if(symbolTable.levelOf(identName) == 0){
			write("PUSHI, " + symbolTable.addressOf(identName)); //variable im Hauptprogramm
		}
		else{
			write("PUSHI, " + symbolTable.levelOf(identName));
			write("GETSL");
			write("PUSHI, " + symbolTable.addressOf(identName));
			write("ADD");
		}
		return null;
	}
}
