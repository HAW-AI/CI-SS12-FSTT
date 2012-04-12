package haw.ai.ci;

public class Parser {
	  public static void main(String argv[]) {
		    if (argv.length == 0) {
		      System.out.println("Usage : java MyScanner <inputfile>");
		    }
		    else {
		      for (int i = 0; i < argv.length; i++) {
		        MyScanner scanner = null;
		        try {
		          scanner = new MyScanner( new java.io.FileReader(argv[i]) );
		          Token t;
		          while ((t = scanner.yylex()) != null) System.out.println(t);
		        }
		        catch (java.io.FileNotFoundException e) {
		          System.out.println("File not found : \""+argv[i]+"\"");
		        }
		        catch (java.io.IOException e) {
		          System.out.println("IO error scanning file \""+argv[i]+"\"");
		          System.out.println(e);
		        }
		        catch (Exception e) {
		          System.out.println("Unexpected exception:");
		          e.printStackTrace();
		        }
		      }
		    }
		  }
}
