package haw.ai.ci.node;

public class RecordSelectorNode extends SelectorNode {
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode subject;
    private final AbstractNode selector;

    public RecordSelectorNode(IdentNode subject, AbstractNode selector) {
        this((AbstractNode)subject, selector);
    }
    
    public RecordSelectorNode(SelectorNode subject, AbstractNode selector) {
        this((AbstractNode)subject, selector);
    }

    private RecordSelectorNode(AbstractNode subject, AbstractNode selector) {
        this.subject = subject;
        this.selector = selector;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "RecordSelectorNode\n")
                + subject.toString(indent + 1) + "\n"
                + selector.toString(indent + 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((selector == null) ? 0 : selector.hashCode());
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
        RecordSelectorNode other = (RecordSelectorNode) obj;
        if (selector == null) {
            if (other.selector != null)
                return false;
        } else if (!selector.equals(other.selector))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }
    
    
    
}
