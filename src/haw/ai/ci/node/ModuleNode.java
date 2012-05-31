package haw.ai.ci.node;

public class ModuleNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final IdentNode ident;
	private final AbstractNode declaration;
	private final AbstractNode statementSequence;

	public ModuleNode(IdentNode ident, AbstractNode declaration,
			AbstractNode statementSequence) {
		this.ident = ident;
		this.declaration = declaration;
		this.statementSequence = statementSequence;
	}

	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ModuleNode\n");
		if(ident != null)
        	str += ident.toString(indent+1) + "\n";
		if(declaration != null)
	    	str += declaration.toString(indent+1) + "\n";
		if(statementSequence != null)
	    	str += statementSequence.toString(indent+1) + "\n";
    	return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		result = prime
				* result
				+ ((statementSequence == null) ? 0 : statementSequence
						.hashCode());
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
		ModuleNode other = (ModuleNode) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		if (statementSequence == null) {
			if (other.statementSequence != null)
				return false;
		} else if (!statementSequence.equals(other.statementSequence))
			return false;
		return true;
	}

}