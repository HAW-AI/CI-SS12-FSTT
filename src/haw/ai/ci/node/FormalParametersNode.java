package haw.ai.ci.node;

import haw.ai.ci.SymbolTable;
import haw.ai.ci.descriptor.Descriptor;

import java.util.ArrayList;
import java.util.List;

public class FormalParametersNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
    private final List<FPSectionNode> fpsections;
    public FormalParametersNode(List<FPSectionNode> fpsections) {
    	this.fpsections=new ArrayList<FPSectionNode>(fpsections);
    }
    @Override
    protected String toString(int indent) {
        String str = toString(indent, "FormalParametersNode\n");
        for (FPSectionNode node : fpsections) {
            str += node.toString(indent+1)+"\n";
        }
        return str;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fpsections == null) ? 0 : fpsections.hashCode());
        result = prime * result + ((fpsections == null) ? 0 : fpsections.hashCode());
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
        FormalParametersNode other = (FormalParametersNode) obj;
        if (fpsections == null) {
            if (other.fpsections != null)
                return false;
        } else if (!fpsections.equals(other.fpsections))
            return false;
        return true;
    }
    
    public Descriptor compile(SymbolTable table){
    	for(FPSectionNode node : fpsections){
    		node.compile(table);
    	}
    	return null;
    } 
    
    public int size(){
    	int size = 0;
    	for(FPSectionNode node : fpsections){
    		size += node.size();
    	}
    	return size;
    }
    
}
