package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

public class RecordDescriptor implements Descriptor {
    private SymbolTable fields;
    
    public RecordDescriptor(SymbolTable fields) {
        this.fields = fields;
    }
    
    public int size() {
        return fields.size();
    }
}
