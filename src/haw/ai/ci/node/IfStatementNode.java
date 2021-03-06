package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;


public class IfStatementNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final AbstractNode exp1;
    private final AbstractNode stateSeq1;
    private final AbstractNode stateSeq2;
    private final AbstractNode elseIfs;
    
    public IfStatementNode(AbstractNode exp1,AbstractNode stateSeq1,AbstractNode elseIfs,AbstractNode stateSeq2){
    	this.exp1 = exp1;
    	this.stateSeq1 = stateSeq1;
    	this.elseIfs = elseIfs;
    	this.stateSeq2 = stateSeq2;
    }
    
    public int getCountOfElse(){
    	return (elseIfs!=null?(((IfStatementNode)elseIfs).getCountOfElse()+1):0) + (stateSeq2==null?0:1);
    }
	
    @Override
	protected String toString(int indent) {
        String str = toString(indent, "IfStatementNode\n");
        if(exp1 != null)
        str += exp1.toString(indent+1) + "\n";
        if(stateSeq1 != null)
        str += stateSeq1.toString(indent+1) + "\n";
        if(elseIfs != null)
        	str += elseIfs.toString(indent+1) + "\n";
        if(stateSeq2 != null)
        	str += stateSeq2.toString(indent+1) + "\n";
        return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elseIfs == null) ? 0 : elseIfs.hashCode());
		result = prime * result + ((exp1 == null) ? 0 : exp1.hashCode());
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
		if (elseIfs == null) {
			if (other.elseIfs != null)
				return false;
		} else if (!elseIfs.equals(other.elseIfs))
			return false;
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
		if (stateSeq2 == null) {
			if (other.stateSeq2 != null)
				return false;
		} else if (!stateSeq2.equals(other.stateSeq2))
			return false;
		return true;
	}
	public void compileElseIf(SymbolTable symbolTable,int end){
		exp1.compile(symbolTable);
		write("BF, " + labelCount);
		stateSeq1.compile(symbolTable);
		write("JMP, " + end);
		if (elseIfs != null){
			write("LABEL, " + AbstractNode.getNextLabelNumber());
			((IfStatementNode)elseIfs).compileElseIf(symbolTable,end);
		}
	}
	public Descriptor compile(SymbolTable symbolTable){
		exp1.compile(symbolTable);
		write("BF, " + labelCount);
		stateSeq1.compile(symbolTable);
		int end=(AbstractNode.labelCount+getCountOfElse());
		write("JMP, " + end);
		if (elseIfs != null){
			write("LABEL, " + AbstractNode.getNextLabelNumber());
			((IfStatementNode)elseIfs).compileElseIf(symbolTable,end);
		}
		if (stateSeq2 != null){
			write("LABEL, " + AbstractNode.getNextLabelNumber());
			stateSeq2.compile(symbolTable);
		}
		write("LABEL, " + AbstractNode.getNextLabelNumber());
		return null;
	}

}
