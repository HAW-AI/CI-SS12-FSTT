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
    private final int linkage = 3;
    

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
		
		SymbolTable lokal = new SymbolTable(symbolTable);
		int label = getNextLabelNumber();
		
		if(fparams != null){
			fparams.compile(lokal);
		}
		
		if(declarations != null){
			declarations.compile(lokal);
		}
		int allocatedMemory = linkage + lokal.size();
		write("LABEL, " + label);
		
		//entry Code starts here
		write("INIT, " + allocatedMemory);
		write("PUSHREG, RK");
		write("PUSHREG, FP");
		//SL Register retten
		write("PUSHI, " + lokal.currentLvl());
		write("PUSHREG, SL");
		//FP := SP
		write("GETSP");
		write("SETFP");
		//SL := FP
		write("GETFP");
		write("PUSHI, " + lokal.currentLvl());
		write("SETSL");
		//SP := SP + lokale variablen
		write("GETSP");
		write("PUSHI, " + lokal.size());
		write("ADD");
		write("SETSP");
		//end of entryCode
		
		
		if(statseq != null){
			statseq.compile(lokal);
		}else{
			error("Kein StatementSequenzNode");
		}
		
		//exitCode starts here
		//SP := FP
		write("GETFP");
		write("SETSP");
		//Restore SL
		write("PUSHI, " + lokal.currentLvl());
		write("POPREG, SL");
		write("POPREG, FP");
		write("POPREG, RK");
		//SP := SP - Parameter
		write("GETSP");
		write("PUSHI, "+ fparams.size() );
		write("SUB");
		write("SETSP");
		
		int reduceVal = allocatedMemory + fparams.size();
		write("REDUCE, " + reduceVal);
		write("RET");
		
				

		ProcDescriptor descr = new ProcDescriptor(label,lokal);
		symbolTable.declare(ident.getIdentName(),descr);
		return descr; 
		
		
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
