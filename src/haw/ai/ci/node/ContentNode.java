package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class ContentNode extends AbstractNode {
    
    private static final long serialVersionUID = 1L;
    
    private AbstractNode subject;
    
    public ContentNode(IdentNode ident) {
        this((AbstractNode)ident);
    }
    
    public ContentNode(SelectorNode selector) {
        this((AbstractNode)selector);
    }
    
    private ContentNode(AbstractNode subject) {
        this.subject = subject;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "ContentNode(" + subject + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        ContentNode other = (ContentNode) obj;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }
    
    @Override
    public Descriptor compile(SymbolTable symbolTable) {
        subject.compile(symbolTable);
		write("CONT, 1");
		return null;
    }

}
