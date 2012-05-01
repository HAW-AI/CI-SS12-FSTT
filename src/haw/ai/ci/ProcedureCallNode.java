package haw.ai.ci;

public class ProcedureCallNode extends AbstractNode {

    private final IdentNode ident;
    private final AbstractNode actualParameters;
	
	ProcedureCallNode(IdentNode ident,AbstractNode actualParameters){
		this.ident = ident;
		this.actualParameters = actualParameters;
	}
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ProcedureCallNode\n");
        str += ident.toString(indent+1) + "\n";
        str += actualParameters.toString(indent+1);
        return str;
	}

}
