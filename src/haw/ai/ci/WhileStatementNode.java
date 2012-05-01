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

}
