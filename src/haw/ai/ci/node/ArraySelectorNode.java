package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.ArrayDescriptor;
import haw.ai.ci.descriptor.Descriptor;

public class ArraySelectorNode extends SelectorNode {
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode subject;
    private final AbstractNode selector;

    public ArraySelectorNode(IdentNode subject, AbstractNode selector) {
        this((AbstractNode)subject, selector);
    }
    
    public ArraySelectorNode(SelectorNode subject, AbstractNode selector) {
        this((AbstractNode)subject, selector);
    }
    
    private ArraySelectorNode(AbstractNode subject, AbstractNode selector) {
        this.subject = subject;
        this.selector = selector;
    }

    @Override
    protected String toString(int indent) {
        return toString(indent, "ArraySelectorNode\n")
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
        ArraySelectorNode other = (ArraySelectorNode) obj;
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
    
    public Descriptor compile(SymbolTable table){
    	ArrayDescriptor d;
    	int typeSize;
    	if(subject instanceof IdentNode){
    		d = (ArrayDescriptor) table.descriptorFor(((IdentNode)subject).getIdentName());
    		typeSize = d.basetype().size();
    		subject.compile(table);
    		selector.compile(table);
    		write("PUSHI, " + typeSize);
    		write("MUL");    		
    		write("ADD");
    	}else{
    		d = (ArrayDescriptor)subject.compile(table);
    		typeSize = d.basetype().size();
    		selector.compile(table);
    		write("PUSHI, "+ typeSize);
    		write("MUL");
    		write("ADD");
    	}
    	return d.basetype();
    }
    
}
