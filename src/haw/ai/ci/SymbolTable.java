package haw.ai.ci;

import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor;
import haw.ai.ci.descriptor.SimpleTypeDescriptor.Type;

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
	
	public boolean hasParentTable(){
		return (this.parentTable != null);
	}
	
	public int currentLvl(){
		if(this.parentTable == null) return 0;
		else return 1+parentTable.currentLvl();
	}
	
	public int levelOf(String ident){
		int i;
		if(descriptorMap.containsKey(ident)){
			i = 0;
		}
		else{
			if(this.parentTable == null) error("variable nicht gefunden:" + ident);
			i = 1 + parentTable.levelOf(ident);
		}
		return this.currentLvl() - i;
	}

	
	public void declare(String ident, Descriptor descr) {
		if(!(addressMap.containsKey(ident))){
			descriptorMap.put(ident, descr);
			addressMap.put(ident, currentAddress);
			if(descr == null){
				System.out.println("---- compile Error-----\n Variable = " + ident);
			}
			currentAddress += descr.size();
		}else{
			System.out.println("Fehler, zweimal die gleiche Variable deklariert");
		}
	}
	


	public Descriptor descriptorFor(String ident) {
	    // built-in types
	    if (ident.equalsIgnoreCase("integer")) {
	        return new SimpleTypeDescriptor(Type.INTEGER);
	    }
	    
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
	
	public String toString(){
		StringBuffer result = new StringBuffer();
		for(Map.Entry<String, Integer> e : addressMap.entrySet()){
			result.append(e.getKey());
			result.append(" : ");
			result.append(e.getValue());
			result.append("\n");
		}
		result.append("\n");
		
		for(Map.Entry<String, Descriptor> e : descriptorMap.entrySet()){
			result.append(e.getKey());
			result.append(" : ");
			result.append(e.getValue());
			result.append("\n");
		}
		result.append("\n");
		
		result.append("\n");
		
		result.append("currentAddress: " + currentAddress + "\n");
		if(parentTable != null){
			result.append("-----------------------------------------\n\n");
			result.append("parentTable: \n" + parentTable + "\n");
		}
		
		return result.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressMap == null) ? 0 : addressMap.hashCode());
		result = prime * result + currentAddress;
		result = prime * result + ((descriptorMap == null) ? 0 : descriptorMap.hashCode());
		result = prime * result + ((parentTable == null) ? 0 : parentTable.hashCode());
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
		SymbolTable other = (SymbolTable) obj;
		if (addressMap == null) {
			if (other.addressMap != null)
				return false;
		} else if (!addressMap.equals(other.addressMap))
			return false;
		if (currentAddress != other.currentAddress)
			return false;
		if (descriptorMap == null) {
			if (other.descriptorMap != null)
				return false;
		} else if (!descriptorMap.equals(other.descriptorMap))
			return false;
		if (parentTable == null) {
			if (other.parentTable != null)
				return false;
		} else if (!parentTable.equals(other.parentTable))
			return false;
		return true;
	}
	
	static void error(String str) {
		throw new ParserException("==> Error: " + str);
	}

	public static void main(String args[]){
		System.out.println(SymbolTable.class.getClassLoader());
		System.out.println(SymbolTable.class.getClassLoader());
	}

}