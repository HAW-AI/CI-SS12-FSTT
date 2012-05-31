package haw.ai.ci.node;

public class BinOpNode extends AbstractNode {
    private static final long serialVersionUID = 1L;

    public enum BinOp {
        MUL_OP, DIV_OP, PLUS_OP, MINUS_OP,
        EQ_OP, NEQ_OP, LO_OP, LOEQ_OP, HI_OP, HIEQ_OP
    }

    private final BinOp op;
    private final AbstractNode left, right;

    public BinOpNode(BinOp op, AbstractNode left, AbstractNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    protected String toString(int indent) {
        return toString(indent, "BinOpNode(" + op + ")") + "\n" +
            left.toString(indent+1) + "\n" +
            right.toString(indent+1);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((op == null) ? 0 : op.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
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
        BinOpNode other = (BinOpNode) obj;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (op != other.op)
            return false;
        if (right == null) {
            if (other.right != null)
                return false;
        } else if (!right.equals(other.right))
            return false;
        return true;
    }


    /**
     * f�r die erstellung der symboltabelle
     */
	public Object getVal() {
		if (op.equals(BinOp.MUL_OP)){
			return (Integer)left.getVal() * (Integer)right.getVal();
		}
		else if (op.equals(BinOp.DIV_OP)){
			return (Integer)left.getVal() / (Integer)right.getVal();
		}
		else if (op.equals(BinOp.MINUS_OP)){
			return (Integer)left.getVal() - (Integer)right.getVal();
		}
		else if (op.equals(BinOp.PLUS_OP)){
			if (left.getVal() instanceof Integer){
				return (Integer)left.getVal() + (Integer)right.getVal();
			}
			else {
				return (String)left.getVal() + (String)right.getVal();
			}
		}
		else {
			return null;
		}
	}
    
    
   

}
