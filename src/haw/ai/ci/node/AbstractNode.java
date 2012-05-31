package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.io.Serializable;

public abstract class AbstractNode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String toString() {
        return toString(0);
    }
    
    protected abstract String toString(int indent);
    
    protected String toString(int indent, String str) {
        String indentStr = "";
        for (int i = 0; i < indent; ++i) {
            indentStr += "  ";
        }
        
        return str.replaceAll("^", indentStr);
    }
    
    public Descriptor compile(SymbolTable symbolTable){
		return null;
    }
    
    public Descriptor compile(SymbolTable symbolTable, Descriptor desct){
    	return null;
    }

}
