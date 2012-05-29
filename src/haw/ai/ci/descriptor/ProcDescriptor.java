package haw.ai.ci.descriptor;

import haw.ai.ci.SymbolTable;

import java.util.List;

public class ProcDescriptor implements Descriptor{

	private SymbolTable symboltable;
	
	public ProcDescriptor(SymbolTable params){
		this. symboltable = params;
	}
	
	public SymbolTable getSymbolTable(){
		return this.symboltable;
	}
	
	public int size(){
		return symboltable.size()/*+ framesize ? */;
	}
}
