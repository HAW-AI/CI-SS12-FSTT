package haw.ai.ci.descriptor;

public class IntConstDescriptor implements Descriptor {

	int value;
	
	public IntConstDescriptor(int value) {
		this.value = value;
	}

	@Override
	public int size() {
		return 0;
	}
	
	public int value(){
		return this.value;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof IntConstDescriptor)) return false;
		IntConstDescriptor d = (IntConstDescriptor)o;
		return (this.value == d.value());
	}
	
	public int hashCode(){
		int result = 7;
		result = 31 * result + value;
		return result;
	}
}
