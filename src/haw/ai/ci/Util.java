package haw.ai.ci;

public class Util {
    public static String indentString(int indent, String str) {
        String indentStr = "";
        for (int i = 0; i < indent; ++i) {
            indentStr += "  ";
        }
        
        return str.replaceAll("^", indentStr);
    }
}
