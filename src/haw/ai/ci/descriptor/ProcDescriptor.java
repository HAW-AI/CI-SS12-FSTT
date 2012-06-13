package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

import java.util.List;

public class ProcDescriptor implements Descriptor{

	private int labelInAssembler;
	private SymbolTable lokal;
	private List<Descriptor> parameters;
	
	public ProcDescriptor(int labelInAssembler, SymbolTable lokal, List<Descriptor> parameters){
		this.labelInAssembler = labelInAssembler;
		this.lokal = lokal;
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + labelInAssembler;
		result = prime * result + ((lokal == null) ? 0 : lokal.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}
	

	public int getLabelInAssembler() {
		return labelInAssembler;
	}

	public SymbolTable getLokal() {
		return lokal;
	}

	public List<Descriptor> getParameters() {
		return parameters;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcDescriptor other = (ProcDescriptor) obj;
		if (labelInAssembler != other.labelInAssembler)
			return false;
		if (lokal == null) {
			if (other.lokal != null)
				return false;
		} else if (!lokal.equals(other.lokal))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}

	@Override
	public int size() {
		return 0; //macht keinen sinn
	}
}
