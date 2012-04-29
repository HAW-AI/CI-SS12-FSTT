package haw.ai.ci;

import static haw.ai.ci.Util.indentString;

import java.io.Serializable;

public abstract class AbstractNode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String toString() {
        return toString(0);
    }
    
    protected abstract String toString(int indent);
    
    protected String toString(int indent, String str) {
        return indentString(indent, str);
    }

}
