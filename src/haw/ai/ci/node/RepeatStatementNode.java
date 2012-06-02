package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

public class RepeatStatementNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final AbstractNode exp1;
    private final AbstractNode stateSeq1;
    
    public RepeatStatementNode(AbstractNode stateSeq1,AbstractNode exp1){
    	this.exp1 = exp1;
    	this.stateSeq1 = stateSeq1;
    	
    }
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "RepeatStatement\n");
        if(stateSeq1 != null)
        str += stateSeq1.toString(indent+1) + "\n";
        if(exp1 != null)
        str += exp1.toString(indent+1) + "\n";
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
	
	public Descriptor compile(SymbolTable table){
		int label = getNextLabelNumber();
		write("LABEL, "+ label);
		stateSeq1.compile(table);
		exp1.compile(table);
		write("BF, "+label);
		return null;
		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepeatStatementNode other = (RepeatStatementNode) obj;
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
