package haw.ai.ci.descriptor;

public class SimpleTypeDescriptor implements Descriptor{
	
	final public static String STRING="string";
	final public static String BOOLEAN="boolean";
	final public static String INTEGER="integer";
	private String type;
	
	public SimpleTypeDescriptor(String type) {
		this.type=type;
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	public String type(){
		return this.type;
	}
}
