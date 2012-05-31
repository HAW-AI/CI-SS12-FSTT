package haw.ai.ci.node;

public class ContentNode extends AbstractNode {
    
    private static final long serialVersionUID = 1L;
    
    private IdentNode ident;
    
    public ContentNode(IdentNode ident) {
        this.ident = ident;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "ContentNode(" + ident + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
        ContentNode other = (ContentNode) obj;
        if (ident == null) {
            if (other.ident != null)
                return false;
        } else if (!ident.equals(other.ident))
            return false;
        return true;
    }

}
