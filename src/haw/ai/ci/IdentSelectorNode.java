package haw.ai.ci;

public class IdentSelectorNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final IdentNode subject;
    private final IdentNode field;
    
    public IdentSelectorNode(IdentNode subject, IdentNode field) {
        this.subject = subject;
        this.field = field;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "IdentSelectorNode(" + subject + ", " + field +")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((field == null) ? 0 : field.hashCode());
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
        IdentSelectorNode other = (IdentSelectorNode) obj;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }

}
