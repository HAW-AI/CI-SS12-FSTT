package haw.ai.ci;

import java.util.List;

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
	

	// TODO hash equals

}
