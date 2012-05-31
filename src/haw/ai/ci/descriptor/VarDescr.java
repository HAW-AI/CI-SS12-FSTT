package haw.ai.ci

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
}
