package haw.ai.ci;

class Token {
	private int line, column;
	private String text;
	private TokenID id;
	
	public Token(TokenID id, String text, int line, int column) {
		this.id = id;
		this.text = text;
		this.line = line;
		this.column = column;
		
        String out = "Token(" + id + "(" + id.id() + ")" + "," + text + "," + line + "," + column + ")";
		System.out.println(out);
	}
	
	public TokenID id() { return id; }
	public String text() { return text; }
	public int line() { return line; }
	public int column() { return column; }
}