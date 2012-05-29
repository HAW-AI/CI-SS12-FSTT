package haw.ai.ci;

import haw.ai.ci.descriptor.Descriptor;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String, Integer> addressMap = new HashMap<String, Integer>();
	private Map<String, Descriptor> descriptorMap = new HashMap<String,Descriptor>();
	private int currentAddress = 0;
	private SymbolTable parentTable;
	
	public SymbolTable(){
		this.parentTable = null;
	}
	
	public SymbolTable(SymbolTable table){
		this.parentTable = table;
	}
	
	public static SymbolTable createSymbolTable(){
		return new SymbolTable();
	}
	

	
	public void declare(String ident, Descriptor descr) {
		descriptorMap.put(ident, descr);
		addressMap.put(ident, currentAddress);
		currentAddress += descr.size();
	}
	
//	public void undeclare(String ident){
//		descriptorMap.remove(ident);
//		addressMap.remove(ident);
//		
//	}


	public Descriptor descriptorFor(String ident) {
		Descriptor d = descriptorMap.get(ident);
		if(d == null && parentTable != null){
			return parentTable.descriptorFor(ident);
		}
		return d;
	}

	

	
	public int addressOf(String ident) {
		
		if(addressMap.containsKey(ident)){
			return addressMap.get(ident);
		}
		if(parentTable != null){
			return parentTable.addressOf(ident);
		}
		return -1;
		
		
	}

	
	public int size() {
		return currentAddress;
	}
	
	public static void main(String args[]){
		System.out.println(SymbolTable.class.getClassLoader());
		System.out.println(SymbolTable.class.getClassLoader());
	}

}
