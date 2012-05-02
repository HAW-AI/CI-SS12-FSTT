package haw.ai.ci;

import java.util.ArrayList;
import java.util.List;

public class RecordTypeNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
    private final List<FieldListNode> fieldLists;
    public RecordTypeNode(List<FieldListNode> fieldLists) {
    	this.fieldLists=new ArrayList<FieldListNode>(fieldLists);
    }
    @Override
    protected String toString(int indent) {
        return toString(indent, "RecordTypeNode("+ fieldLists + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fieldLists == null) ? 0 : fieldLists.hashCode());
        result = prime * result + ((fieldLists == null) ? 0 : fieldLists.hashCode());
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
        RecordTypeNode other = (RecordTypeNode) obj;
        if (fieldLists == null) {
            if (other.fieldLists != null)
                return false;
        } else if (!fieldLists.equals(other.fieldLists))
            return false;
        return true;
    }
    
}
