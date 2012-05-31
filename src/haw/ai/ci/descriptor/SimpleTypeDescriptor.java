package haw.ai.ci.descriptor;

public class SimpleTypeDescriptor implements Descriptor{
	
	public enum Type{
		STRING, BOOLEAN, INTEGER;
	}
	private Type type;
	
	public SimpleTypeDescriptor(Type type) {
		this.type=type;
	}
	
	@Override
	public int size() {
		return 1;
	}
}
