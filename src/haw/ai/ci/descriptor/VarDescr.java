package haw.ai.ci.descriptor;

public class VarDescr implements Descriptor {

	int addr;

	Descriptor typ;

	public VarDescr(){
		addr = 0;
		typ = null;
	}

	public VarDescr(int fa, Descriptor ftyp){
		addr = fa;
		typ = ftyp;
	}

	public int getAddr(){
		return addr;
	}

	public Descriptor getTyp(){
		return typ;
	}
	
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + addr;
		result = prime * result + ((typ == null) ? 0 : typ.hashCode());
		return result;		
	}
	
	public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof VarDescr)) return false;
		VarDescr d = (VarDescr) o;
		return (this.addr == d.getAddr()) && (this.typ.equals(d.getTyp()));
	}
	
	public String toString(){
		return "VarDescr[address: " + addr + " , typ: " + typ.toString() + " ]";
	}

	@Override
	public int size() {
		return -1;
	}
}
