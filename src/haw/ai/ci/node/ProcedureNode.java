package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.ProcDescriptor;

public class ProcedureNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	// dec
	private final IdentNode ident;
	// heading
	private final IdentNode subject;
    private final FormalParametersNode fparams;
    // body
	private final DeclarationsNode declarations;
    private final AbstractNode statseq;
    

	public ProcedureNode(IdentNode declEndIdent, IdentNode subject, FormalParametersNode fparams,
			DeclarationsNode declarations, AbstractNode statseq) {
		super();
		this.ident = declEndIdent;
		this.subject = subject;
		this.fparams = fparams;
		this.declarations = declarations;
		this.statseq = statseq;
	}
	
	@Override
	public Descriptor compile(SymbolTable symbolTable){
		SymbolTable sm = new SymbolTable(symbolTable);
		
		if(fparams != null)
			fparams.compile(sm);
		if(declarations != null)
			declarations.compile(sm);
		if(statseq != null)
			statseq.compile(sm);
		write("REDUCE, " + declarations.size());
		
		return new ProcDescriptor(sm);
	}
	
	@Override
	protected String toString(int indent) {

		String result = toString(indent, "Procedure");
//		// decl
//		return toString(indent, "ProcedureDeclaration\n")
//				+ procHeading.toString(indent + 1) + "\n"
//				+ procBody.toString(indent + 1);
//		// head
//		String str = toString(indent, "ProcedureHeadingNode(" + subject + ")");
		result += "( " + subject + ")\n";
//		if (fparams != null) {
//		    str += "\n" + fparams.toString(indent + 1);
//		}
		if (fparams != null) {
			result += "\n" + fparams.toString(indent + 1);
		}
//		return str;
//		// body
//		String str = toString(indent, "ProcedureBodyNode\n");
//		if(declarations != null)
//			str += declarations.toString(indent+1) + "\n";
		if(declarations != null)
			result += declarations.toString(indent+1) + "\n";
//		if(statseq != null)
//			str += statseq.toString(indent+1) + "\n";
		if(statseq != null)
			result += statseq.toString(indent+1) + "\n";
//		return str;
		
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declarations == null) ? 0 : declarations.hashCode());
		result = prime * result + ((fparams == null) ? 0 : fparams.hashCode());
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		result = prime * result + ((statseq == null) ? 0 : statseq.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		ProcedureNode other = (ProcedureNode) obj;
		if (declarations == null) {
			if (other.declarations != null)
				return false;
		} else if (!declarations.equals(other.declarations))
			return false;
		if (fparams == null) {
			if (other.fparams != null)
				return false;
		} else if (!fparams.equals(other.fparams))
			return false;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		if (statseq == null) {
			if (other.statseq != null)
				return false;
		} else if (!statseq.equals(other.statseq))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	


}
