package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

public class RecordDescriptor implements Descriptor {
    private SymbolTable fields;
    
    public RecordDescriptor(SymbolTable fields) {
        this.fields = fields;
    }
    
    public SymbolTable fields(){
    	return this.fields;
    }
    
    public int size() {
        return fields.size();
    }
    
    public String toString(){
    	StringBuffer b = new StringBuffer();
    	b.append("[RecordDescriptor: ");
    	b.append(fields.toString());
    	b.append("] ");
    	return b.toString();
    }
    
    public Descriptor descriptorFor(String s){
    	return fields.descriptorFor(s);
    }
    
    public int addressOf(String s){
    	return fields.addressOf(s);
    }
    
    public boolean equals(Object o){
    	if(!(o instanceof RecordDescriptor)) return false;
    	RecordDescriptor d = (RecordDescriptor)o;
    	return this.fields.equals(d.fields());
    }
}
