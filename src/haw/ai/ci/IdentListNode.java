package haw.ai.ci;

import java.util.ArrayList;
import java.util.List;

public class IdentListNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
    private final List<IdentNode> idents;
    public IdentListNode(List<IdentNode> idents) {
    	this.idents=new ArrayList<IdentNode>(idents);
    }
    @Override
    protected String toString(int indent) {
        return toString(indent, "IdentListNode("+ idents + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((idents == null) ? 0 : idents.hashCode());
        result = prime * result + ((idents == null) ? 0 : idents.hashCode());
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
        IdentListNode other = (IdentListNode) obj;
        if (idents == null) {
            if (other.idents != null)
                return false;
        } else if (!idents.equals(other.idents))
            return false;
        return true;
    }
    
}
