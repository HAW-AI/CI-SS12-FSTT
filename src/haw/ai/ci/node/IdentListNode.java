package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.util.ArrayList;
import java.util.List;

public class IdentListNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
    private final List<IdentNode> idents;
    public IdentListNode(List<IdentNode> idents) {
    	this.idents=new ArrayList<IdentNode>(idents);
    }
    @Override
    protected String toString(int indent) {
        String str = toString(indent, "IdentListNode");
        for (AbstractNode node : idents) {
            str += "\n" + node.toString(indent+1);
        }
        return str;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((idents == null) ? 0 : idents.hashCode());
        result = prime * result + ((idents == null) ? 0 : idents.hashCode());
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
        IdentListNode other = (IdentListNode) obj;
        if (idents == null) {
            if (other.idents != null)
                return false;
        } else if (!idents.equals(other.idents))
            return false;
        return true;
    }
    
    public Descriptor compile(SymbolTable table, Descriptor descriptor){
    	for(IdentNode node : idents){
    		table.declare(node.getIdentName(), descriptor);
    	}
    	return null; //schreibt nur in die Symboltabelle --> kein geeigneter Ruechgabewert vorhanden
    }
    
    public Descriptor compileParams(SymbolTable table, Descriptor descriptor){
    	for(IdentNode node : idents){
    		table.declareParams(node.getIdentName(), descriptor);
    	}
    	return null;
    }
    
	public int size() {
		return idents.size();
	}
    
}
