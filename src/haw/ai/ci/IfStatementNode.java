package haw.ai.ci;

import java.util.List;

public class IfStatementNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final AbstractNode exp1;
    private final AbstractNode stateSeq1;
    private final AbstractNode stateSeq2;
//	abwechselnd expr und statementSeq
    private final List<AbstractNode> list;
    
    IfStatementNode(AbstractNode exp1,AbstractNode stateSeq1,List<AbstractNode> list,AbstractNode stateSeq2){
    	this.exp1 = exp1;
    	this.stateSeq1 = stateSeq1;
    	this.list = list;
    	this.stateSeq2 = stateSeq2;
    }
    
    
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "IfStatementNode\n");
        str += exp1.toString(indent+1) + "\n";
        str += stateSeq1.toString(indent+1) + "\n";

        for(int i = 0; i < list.size();i++){
        str += list.get(i).toString(indent+1) + "\n";
        }
        str += stateSeq2.toString(indent+1) + "\n";
        
        return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exp1 == null) ? 0 : exp1.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((stateSeq1 == null) ? 0 : stateSeq1.hashCode());
		result = prime * result + ((stateSeq2 == null) ? 0 : stateSeq2.hashCode());
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
		IfStatementNode other = (IfStatementNode) obj;
		if (exp1 == null) {
			if (other.exp1 != null)
				return false;
		} else if (!exp1.equals(other.exp1))
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (stateSeq1 == null) {
			if (other.stateSeq1 != null)
				return false;
		} else if (!stateSeq1.equals(other.stateSeq1))
			return false;
		if (stateSeq2 == null) {
			if (other.stateSeq2 != null)
				return false;
		} else if (!stateSeq2.equals(other.stateSeq2))
			return false;
		return true;
	}

}
