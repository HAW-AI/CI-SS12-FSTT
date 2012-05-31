package haw.ai.ci.descriptor;

public class SimpleTypeDescriptor implements Descriptor{
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SimpleTypeDescriptor other = (SimpleTypeDescriptor) obj;
		if (type != other.type)
			return false;
		return true;
	}

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
	
	public Type type(){
		return this.type;
	}
	
	public String toString(){
		StringBuffer result = new StringBuffer();
		result.append("Type: ");
		result.append(type);
		return result.toString();
	}
}
