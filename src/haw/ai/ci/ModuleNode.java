package haw.ai.ci;

public class ModuleNode extends AbstractNode {

	IdentNode ident;
	AbstractNode declaration;
	AbstractNode statementSequence;
	

	public ModuleNode(IdentNode ident, AbstractNode declaration,
			AbstractNode statementSequence) {
		this.ident = ident;
		this.declaration = declaration;
		this.statementSequence = statementSequence;
	}


	@Override
	protected String toString(int indent) {
		return toString(indent, "Module(" + ident.toString() + ")") + "\n" + declaration.toString(indent+1) + "\n" + statementSequence.toString(indent+1);
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
