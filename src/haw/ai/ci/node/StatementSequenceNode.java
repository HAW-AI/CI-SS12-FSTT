package haw.ai.ci.node;

import java.util.ArrayList;
import java.util.List;

public class StatementSequenceNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final List<? extends AbstractNode> list;
	
	public StatementSequenceNode(List<? extends AbstractNode> list){
		this.list = new ArrayList<AbstractNode>(list);
	}
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "StatementSequence\n");
        for(int i = 0; i < list.size();i++){
        	if(list.get(i) != null )
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
		StatementSequenceNode other = (StatementSequenceNode) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}

}
