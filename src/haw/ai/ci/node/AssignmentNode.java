package haw.ai.ci.node;

public class AssignmentNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final AbstractNode key;
    private final AbstractNode value;

    public AssignmentNode( IdentNode selector,AbstractNode expression) {
        this.key = selector;
        this.value = expression;
    }
    
    public AssignmentNode( SelectorNode selector,AbstractNode expression) {
        this.key = selector;
        this.value = expression;
    }
    
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "AssignmentNode\n");
		if(key != null)
        str += key.toString(indent+1) + "\n";
		if(value != null)
        str += value.toString(indent+1);
        return str;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}