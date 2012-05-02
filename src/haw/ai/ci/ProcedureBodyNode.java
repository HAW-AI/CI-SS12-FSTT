package haw.ai.ci;

public class ProcedureBodyNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode declarations;
    private final AbstractNode statseq;
    
    public ProcedureBodyNode(AbstractNode declarations, AbstractNode statseq) {
        this.declarations = declarations;
        this.statseq = statseq;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "ProcedureBodyNode(" + declarations + ", " + statseq + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((statseq == null) ? 0 : statseq.hashCode());
        result = prime * result + ((declarations == null) ? 0 : declarations.hashCode());
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
        ProcedureBodyNode other = (ProcedureBodyNode) obj;
        if (statseq == null) {
            if (other.statseq != null)
                return false;
        } else if (!statseq.equals(other.statseq))
            return false;
        if (declarations == null) {
            if (other.declarations != null)
                return false;
        } else if (!declarations.equals(other.declarations))
            return false;
        return true;
    }
    
}
