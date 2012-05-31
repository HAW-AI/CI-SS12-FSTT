package haw.ai.ci.descriptor;

public class IntConstDescriptor implements Descriptor {

	int value;
	
	public IntConstDescriptor(int value) {
		this.value = value;
	}

	@Override
	public int size() {
		return 1;
	}
	
	
}
