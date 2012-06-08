package haw.ai.ci.node;

import haw.ai.ci.CompilerException;
import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;
import haw.ai.ci.descriptor.IntConstDescriptor;

public class ConstDeclarationNode extends AbstractNode {

	
    private static final long serialVersionUID = 1L;
    
    private final AbstractNode ident;
    private final AbstractNode expression;
    
    public ConstDeclarationNode(AbstractNode ident,AbstractNode expression){
    	this.ident = ident;
    	this.expression = expression;
    }
	
	
	
	@Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((expression == null) ? 0 : expression.hashCode());
	result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
	ConstDeclarationNode other = (ConstDeclarationNode) obj;
	if (expression == null) {
	    if (other.expression != null)
		return false;
	} else if (!expression.equals(other.expression))
	    return false;
	if (ident == null) {
	    if (other.ident != null)
		return false;
	} else if (!ident.equals(other.ident))
	    return false;
	return true;
    }



	@Override
	protected String toString(int indent) {
        String str = toString(indent, "ConstDeclarationNode\n");
        if(ident != null)
        	str += ident.toString(indent+1) + "\n";
        if(expression != null)
        	str += expression.toString(indent+1) + "\n";
        return str;
	}

	@Override
	public int size() {
		return 1;
	}
	
	public Descriptor compile(SymbolTable table){
		IdentNode id = (IdentNode)ident;
		IntConstDescriptor value = null;
		if(expression instanceof BinOpNode){
			value = new IntConstDescriptor((Integer)((BinOpNode)expression).getVal());
		}
		else if(expression instanceof IntNode){
			value = new IntConstDescriptor(((IntNode)expression).getVal());
		}
		else{
			throw new CompilerException("unsupported value: " + expression);
		}
		table.declare(id.getIdentName(), value);
		return value; //da nur in Tabelle geschrieben wird --> kein geeigneter Rueckgabewert vorhanden
	}

}
