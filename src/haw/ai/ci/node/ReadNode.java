package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class ReadNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final StringNode prompt;

    public ReadNode() {
        this(null);
    }

    public ReadNode(StringNode prompt) {
        this.prompt = prompt;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "Read(" + prompt +")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((prompt == null) ? 0 : prompt.hashCode());
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
        ReadNode other = (ReadNode) obj;
        if (prompt == null) {
            if (other.prompt != null)
                return false;
        } else if (!prompt.equals(other.prompt))
            return false;
        return true;
    }
    
    @Override
    public Descriptor compile(SymbolTable syms) {
        write("READ, " + prompt.getVal());
        
        return null;
    }

}
