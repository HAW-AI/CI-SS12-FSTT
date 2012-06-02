package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class PrintNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private AbstractNode expr;
    
    public PrintNode(AbstractNode expr) {
        this.expr = expr;
    }
    
    @Override
    public Descriptor compile(SymbolTable syms) {
        expr.compile(syms);
        write("PRINT");
        
        return null;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "PrintNode\n" + expr.toString(indent));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
        PrintNode other = (PrintNode) obj;
        if (expr == null) {
            if (other.expr != null)
                return false;
        } else if (!expr.equals(other.expr))
            return false;
        return true;
    }

}
