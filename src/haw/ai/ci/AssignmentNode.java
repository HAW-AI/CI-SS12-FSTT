package haw.ai.ci;

public class AssignmentNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode selector;
    private final AbstractNode expression;
    
    public AssignmentNode( AbstractNode selector,AbstractNode expression) {
        this.selector = selector;
        this.expression = expression;
    }
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "AssignmentNode\n");
		if(selector != null)
        str += selector.toString(indent+1) + "\n";
		if(expression != null)
        str += expression.toString(indent+1);
        return str;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((selector == null) ? 0 : selector.hashCode());
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
		AssignmentNode other = (AssignmentNode) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (selector == null) {
			if (other.selector != null)
				return false;
		} else if (!selector.equals(other.selector))
			return false;
		return true;
	}
	
	// TODO hash equals

}
