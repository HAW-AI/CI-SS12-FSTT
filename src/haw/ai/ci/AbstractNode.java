package haw.ai.ci;

import java.io.Serializable;

public abstract class AbstractNode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String toString() {
        return toString(0);
    }
    
    protected abstract String toString(int indent);
}
