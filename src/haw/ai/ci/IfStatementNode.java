package haw.ai.ci;

import java.util.ArrayList;

public class IfStatementNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final AbstractNode exp1;
    private final AbstractNode stateSeq1;
    private final AbstractNode stateSeq2;
//	abwechselnd expr und statementSeq
    private final ArrayList<AbstractNode> list;
    
    IfStatementNode(AbstractNode exp1,AbstractNode stateSeq1,ArrayList<AbstractNode> list,AbstractNode stateSeq2){
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

        for(int i = 0; 0 < list.size();i++){
        str += list.get(i).toString(indent+1) + "\n";
        }
        str += stateSeq2.toString(indent+1) + "\n";
        
        return str;
	}

}
