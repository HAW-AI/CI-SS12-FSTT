package haw.ai.ci;

public class WhileStatementNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final AbstractNode exp1;
    private final AbstractNode stateSeq1;
    
    WhileStatementNode(AbstractNode exp1,AbstractNode stateSeq1){
    	this.exp1 = exp1;
    	this.stateSeq1 = stateSeq1;
    	
    }
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "WhileStatement\n");
        str += exp1.toString(indent+1) + "\n";
        str += stateSeq1.toString(indent+1) + "\n";
        
        return str;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exp1 == null) ? 0 : exp1.hashCode());
		result = prime * result + ((stateSeq1 == null) ? 0 : stateSeq1.hashCode());
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
		WhileStatementNode other = (WhileStatementNode) obj;
		if (exp1 == null) {
			if (other.exp1 != null)
				return false;
		} else if (!exp1.equals(other.exp1))
			return false;
		if (stateSeq1 == null) {
			if (other.stateSeq1 != null)
				return false;
		} else if (!stateSeq1.equals(other.stateSeq1))
			return false;
		return true;
	}

}
