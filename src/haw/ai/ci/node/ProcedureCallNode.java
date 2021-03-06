package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.ProcDescriptor;

public class ProcedureCallNode extends AbstractNode {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IdentNode ident;
    private final AbstractNode actualParameters;
	
	public ProcedureCallNode(IdentNode ident,AbstractNode actualParameters){
		this.ident = ident;
		this.actualParameters = actualParameters;
	}
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ProcedureCallNode\n");
        if(ident != null)
        str += ident.toString(indent+1) + "\n";
        if(actualParameters != null)
        str += actualParameters.toString(indent+1);
        return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualParameters == null) ? 0 : actualParameters.hashCode());
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
		ProcedureCallNode other = (ProcedureCallNode) obj;
		if (actualParameters == null) {
			if (other.actualParameters != null)
				return false;
		} else if (!actualParameters.equals(other.actualParameters))
			return false;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}
	
	public Descriptor compile(SymbolTable symbolTable){
		ProcDescriptor procedure = (ProcDescriptor) symbolTable.descriptorFor(ident.getIdentName());
		
		// Paramter auf den Stack
		if(actualParameters != null){
			actualParameters.compile(symbolTable);
		}
		
		write("CALL, "+ procedure.getLabelInAssembler() );
		return null;
		
	}

}
