package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

public class ProcDescriptor implements Descriptor{

	private int labelInAssembler;
	private SymbolTable lokal;
	
	public ProcDescriptor(int labelInAssembler, SymbolTable lokal){
		this.labelInAssembler = labelInAssembler;
		this.lokal = lokal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + labelInAssembler;
		return result;
	}
	

	public int getLabelInAssembler() {
		return labelInAssembler;
	}

	public SymbolTable getLokal() {
		return lokal;
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
		return true;
	}

	@Override
	public int size() {
		return 0; //macht keinen sinn
	}
}
