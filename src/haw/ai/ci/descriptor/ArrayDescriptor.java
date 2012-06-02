package haw.ai.ci.descriptor;

public class ArrayDescriptor implements Descriptor {
	private int numberOfElements;
	private Descriptor basetype; 
	
	
	public ArrayDescriptor(int numberOfElements, Descriptor basetype){
		this.numberOfElements = numberOfElements;
		this.basetype = basetype;
	}
	
//	public static Descriptor create(int numberOfElements, Descriptor basetype){
//		return new ArrayDescriptor(numberOfElements, basetype);
//	}
	
	public int numberOfElements(){
		return this.numberOfElements;
	}
	
	public Descriptor basetype(){
		return this.basetype;
	}
	
	public int size(){
		return basetype.size() * numberOfElements;
	}
	
	public int hashCode(){
		int result = 7;
		result = 31 * result + numberOfElements;
		result = 31 * result + basetype.hashCode();
		return result;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ArrayDescriptor)) return false;
		ArrayDescriptor d = (ArrayDescriptor)o;
		return (this.numberOfElements == d.numberOfElements()) && (this.basetype.equals(d.basetype()));
	}
	
	
	
}


