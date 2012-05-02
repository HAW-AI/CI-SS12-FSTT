package haw.ai.ci;

public class ArrayTypeNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode node;
    private final AbstractNode type;
    public ArrayTypeNode(AbstractNode expression, AbstractNode type) {
        this.node = expression;
        this.type=type;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "ArrayTypeNode") + "\n" + node.toString(indent+1)+"\n"+type.toString(indent+1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode()) + ((type == null) ? 0 : type.hashCode());
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
        ArrayTypeNode other = (ArrayTypeNode) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        
        return true;
    }

}
