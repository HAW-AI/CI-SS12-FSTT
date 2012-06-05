package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.io.Serializable;

public abstract class AbstractNode implements Serializable {
	static int labelCount = 1;
	
    private static final long serialVersionUID = 1L;
    
    private String code = "";
    
    @Override
    public String toString() {
        return toString(0);
    }
    
    public static int getNextLabelNumber(){
    	return labelCount++;
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

	public Object getVal(){
		return null;
	}
	
	public void write(String str){
	    code += str + "\n";
		System.out.println(str);
	}
	
	public String code() {
	    return code;
	}
}
