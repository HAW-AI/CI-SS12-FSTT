package haw.ai.ci;

public class ProcedureDeclarationNode extends AbstractNode {

	private final AbstractNode procHeading;
	private final AbstractNode procBody;
	private final IdentNode ident;
	

	public ProcedureDeclarationNode(AbstractNode procHeadingNode,
			AbstractNode procBodyNode, IdentNode ident) {
		super();
		this.procHeading = procHeadingNode;
		this.procBody = procBodyNode;
		this.ident = ident;
	}

	@Override
	protected String toString(int indent) {
		//TODO: kein einrücken da head und body auf gleicher "ebene", richtig?
		return toString(indent, procHeading.toString() + procBody.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((procBody == null) ? 0 : procBody.hashCode());
		result = prime * result
				+ ((procHeading == null) ? 0 : procHeading.hashCode());
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
		ProcedureDeclarationNode other = (ProcedureDeclarationNode) obj;
		if (procBody == null) {
			if (other.procBody != null)
				return false;
		} else if (!procBody.equals(other.procBody))
			return false;
		if (procHeading == null) {
			if (other.procHeading != null)
				return false;
		} else if (!procHeading.equals(other.procHeading))
			return false;
		return true;
	}

}
