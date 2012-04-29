package haw.ai.ci;

import static haw.ai.ci.Util.*;

public class IntNode extends AbstractNode {
    private static final long serialVersionUID = 1L;

    private int intVal;

    public IntNode(int val) {
        intVal = val;
    }

    protected String toString(int indent) {
        return indentString(indent, "IntNode(" + intVal + ")");
    }

}
