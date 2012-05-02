package haw.ai.ci;

public class ProcedureHeadingNode extends AbstractNode {
    private static final long serialVersionUID = 1L;
    
    private final IdentNode subject;
    private final FormalParametersNode fparams;
    
    public ProcedureHeadingNode(IdentNode subject, FormalParametersNode fparams) {
        this.subject = subject;
        this.fparams = fparams;
    }

    @Override
    protected String toString(int indent) {
        String str = toString(indent, "ProcedureHeadingNode(" + subject + ")");
        if (fparams != null) {
            str += "\n" + fparams.toString(indent + 1);
        }
        return str;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fparams == null) ? 0 : fparams.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
        ProcedureHeadingNode other = (ProcedureHeadingNode) obj;
        if (fparams == null) {
            if (other.fparams != null)
                return false;
        } else if (!fparams.equals(other.fparams))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }
    
}
