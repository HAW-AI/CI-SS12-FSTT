package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.util.List;

public class DeclarationsNode extends AbstractNode {

	private static final long serialVersionUID = 1L;

	private final List<? extends AbstractNode> consts;
	private final List<? extends AbstractNode> types;
	private final List<? extends AbstractNode> vars;
	private final List<? extends AbstractNode> procDeclarations;
	int memSize = 0;

	public DeclarationsNode(List<? extends AbstractNode> consts, List<? extends AbstractNode> types, List<? extends AbstractNode> vars, List<? extends AbstractNode> procDeclarations) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consts == null) ? 0 : consts.hashCode());
		result = prime
				* result
				+ ((procDeclarations == null) ? 0 : procDeclarations.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		result = prime * result + ((vars == null) ? 0 : vars.hashCode());
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
		DeclarationsNode other = (DeclarationsNode) obj;
		if (consts == null) {
			if (other.consts != null)
				return false;
		} else if (!consts.equals(other.consts))
			return false;
		if (procDeclarations == null) {
			if (other.procDeclarations != null)
				return false;
		} else if (!procDeclarations.equals(other.procDeclarations))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		if (vars == null) {
			if (other.vars != null)
				return false;
		} else if (!vars.equals(other.vars))
			return false;
		return true;
	}

	@Override
	public int size() {
		return memSize;
	}
	
	public Descriptor compile(SymbolTable table){
		for(AbstractNode constNode : consts){
			memSize += constNode.compile(table).size();
		}
		for(AbstractNode typeNode : types){
			memSize += typeNode.compile(table).size();
		}
		for(AbstractNode varNode : vars){
			memSize += varNode.compile(table).size()*varNode.size(); //TODO: unschön. besser: identlistdescriptor
		}
		for(AbstractNode procNode : procDeclarations){
			procNode.compile(table);
		}
		
		return null;
		
	}
}
