package haw.ai.ci;

import java.util.List;

public class DeclarationsNode extends AbstractNode {

	private static final long serialVersionUID = 1L;

	private final List<? extends AbstractNode> consts;
	private final List<? extends AbstractNode> types;
	private final List<? extends AbstractNode> vars;
	private final List<? extends AbstractNode> procDeclarations;

	DeclarationsNode(List<? extends AbstractNode> consts, List<? extends AbstractNode> types, List<? extends AbstractNode> vars, List<? extends AbstractNode> procDeclarations) {
		this.consts = consts;
		this.types = types;
		this.vars = vars;
		this.procDeclarations = procDeclarations;
	}

	@Override
	protected String toString(int indent) {
		String str = toString(indent, "DeclarationsNode\n");
		for (int i = 0; i < consts.size(); i++) {
			str += consts.get(i).toString(indent + 1) + "\n";
		}
		for (int i = 0; i < types.size(); i++) {
			str += types.get(i).toString(indent + 1) + "\n";
		}
		for (int i = 0; i < vars.size(); i++) {
			str += vars.get(i).toString(indent + 1) + "\n";
		}
		for (int i = 0; i < procDeclarations.size(); i++) {
			str += procDeclarations.get(i).toString(indent + 1) + "\n";
		}

		return str;
	}
}
