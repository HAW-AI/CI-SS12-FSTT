package haw.ai.ci;

public class Util {
    public static String indentString(int indent, String str) {
        String identStr = "";
        for (int i = 0; i < indent; ++i) {
            identStr += "  ";
        }
        
        String[] arr = str.split("\n");
        String res = "";
        for (int i = 0; i < arr.length; ++i) {
            res += identStr + arr[i];
        }
        
        return res;
    }
}
