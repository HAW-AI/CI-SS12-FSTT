package haw.ai.ci.node;

public class IntNode extends AbstractNode {
    private static final long serialVersionUID = 1L;

    private final int intVal;

    public IntNode(int val) {
        intVal = val;
    }


    @Override
    protected String toString(int indent) {
        return toString(indent, "IntNode(" + intVal + ")");
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + intVal;
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
        IntNode other = (IntNode) obj;
        if (intVal != other.intVal)
            return false;
        return true;
    }

}
