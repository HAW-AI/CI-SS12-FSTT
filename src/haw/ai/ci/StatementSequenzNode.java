package haw.ai.ci;

import java.util.ArrayList;

public class StatementSequenzNode extends AbstractNode {

	private static final long serialVersionUID = 1L;
	
	private final ArrayList<AbstractNode> list;
	
	StatementSequenzNode(ArrayList<AbstractNode> list){
		this.list = list;
	}
	
	
	@Override
	protected String toString(int indent) {
        String str = toString(indent, "StatementSequenz\n");
        for(int i = 0; 0 < list.size();i++){
        str += list.get(i).toString(indent+1) + "\n";
        }
        return str;
	}

}
