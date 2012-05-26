package haw.ai.ci;

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
	
}


