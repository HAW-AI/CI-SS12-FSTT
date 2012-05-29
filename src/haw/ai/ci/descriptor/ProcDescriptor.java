package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

import java.util.List;

public class ProcDescriptor implements Descriptor{

	private SymbolTable params;
	
	public ProcDescriptor(SymbolTable params){
		this. params = params;
	}
	
	public SymbolTable getParams(){
		return this.params;
	}
	
	public int size(){
		return params.size()/*+ framesize ? */;
	}
}
