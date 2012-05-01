package haw.ai.ci;

import java.util.ArrayList;
import java.util.List;

public class SelectorNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final IdentNode subject;
    private final List<AbstractNode> selectors;
    
    public SelectorNode(IdentNode subject, List<? extends AbstractNode> selectors) {
        this.subject = subject;
        this.selectors = new ArrayList<AbstractNode>(selectors);
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "SelectorNode(" + subject + ", " + selectors + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((selectors == null) ? 0 : selectors.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
        SelectorNode other = (SelectorNode) obj;
        if (selectors == null) {
            if (other.selectors != null)
                return false;
        } else if (!selectors.equals(other.selectors))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }
    
}
