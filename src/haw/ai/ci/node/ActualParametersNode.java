package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ActualParametersNode extends AbstractNode {

    private static final long serialVersionUID = 1L;
    
    private final List<AbstractNode> list;
    
    public ActualParametersNode(List<AbstractNode> list) {
        this.list = list;
    }
    
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ActualParametersNode\n");
        for(int i = 0; i < list.size();i++){
        	str += list.get(i).toString(indent+1) + "\n";
        }
        return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
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
		ActualParametersNode other = (ActualParametersNode) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}
	
	public Descriptor compile(SymbolTable table){ 
		write("INIT, " +list.size());
		
		//schreibe ergebnisse der Expressions auf den Stack
		ListIterator<AbstractNode> it = list.listIterator(list.size());
		while(it.hasPrevious()){
			AbstractNode actual = it.previous();
			
			//wert der aktuellen expression berechnen
			actual.compile(table); 
			
			//berechneten Wert auf Stack 
			write("GETSP");
			write("ASSIGN, 1");
			
			//Stackpointer aktualisieren
			write("GETSP");
			write("PUSHI, 1");
			write("ADD");
			write("SETSP");
		}
		return null;
	}

}
