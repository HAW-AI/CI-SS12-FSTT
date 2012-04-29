package haw.ai.ci;

public class ExprSelectorNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final IdentNode subject;
    private final AbstractNode selector;
    
    public ExprSelectorNode(IdentNode subject, AbstractNode selector) {
        this.subject = subject;
        this.selector = selector;
    }

    @Override
    protected String toString(int indent) {
        String str = toString(indent, "ExprSelectorNode\n");
        str += subject.toString(indent+1) + "\n";
        str += selector.toString(indent+1);
        return str;
    }

}
