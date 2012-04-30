package haw.ai.ci;

public class NegationNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode node;
    
    public NegationNode(AbstractNode node) {
        this.node = node;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "NegationNode") + "\n" + node.toString(indent+1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode());
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
        NegationNode other = (NegationNode) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        return true;
    }

}
